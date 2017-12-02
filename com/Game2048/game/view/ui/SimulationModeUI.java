package com.Game2048.game.view.ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.HeadlessException;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.Game2048.game.Application;
import com.Game2048.game.config.Constant;
import com.Game2048.game.model.Board;
import com.Game2048.game.model.HighScore;
import com.Game2048.game.model.SimulatedTask;
import com.Game2048.game.view.component.CardPanel;
import com.Game2048.util.FileUtils;
import com.Game2048.util.JsonUtils;
import com.Game2048.util.ListUtils;
import com.Game2048.util.MathUtils;
import com.Game2048.util.StringUtils;

public class SimulationModeUI extends JPanel implements CardPanel {

	private static final long serialVersionUID = 1L;

	public static final URL STUFF_FOLDER = Constant.STUFF;

	private JTextField numberTextField;
	private final ButtonGroup buttonGroup = new ButtonGroup();
	private JProgressBar progressBar;
	private JSlider slider;
	private JLabel depthLabel;
	private JLabel progressLabel;
	private JRadioButton rdbtnLow;
	private JRadioButton rdbtnMedium;
	private JRadioButton rdbtnHigh;
	private JRadioButton rdbtnCustom;
	private JButton btnStart;
	private JCheckBox chckbxClearOnStart;
	private JButton btnCancel;

	private Application mainApp;
	private int depth = Constant.SIMULATED_START_DEPTH;

	private double progressPercent;
	private int nGames;
	private int nCompletedGames;
	private int nWinGames;

	private Semaphore processMutex;
	private Semaphore dbMutex = new Semaphore(1);
	private Long startTime;
	private SimulationModeUI self;

	private static final int MAX_THREAD_POOL = Constant.MAX_THREAD_POOL;
	private ExecutorService executorService;

	private JEditorPane editorPane;
	private StringBuilder editorText = new StringBuilder();

	private List<SimulatedTask> taskList;

	public SimulationModeUI(Application mainApp) {
		self = this;
		this.mainApp = mainApp;
		initialize();
	}

	public void initialize() {
		setLayout();
		createFormPanel();
		createEditorPanel();
		createProgressPanel();
	}

	private void setLayout() {
		setMainApp(mainApp);
		setAlignmentY(Component.TOP_ALIGNMENT);
		setAlignmentX(Component.LEFT_ALIGNMENT);
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 0 };
		gridBagLayout.rowHeights = new int[] { 0, 0, 30 };
		gridBagLayout.columnWeights = new double[] { 1.0 };
		gridBagLayout.rowWeights = new double[] { 0.2, 1.0, 0 };
		setLayout(gridBagLayout);
	}

	private void createFormPanel() {
		JPanel formPanel = new JPanel();
		formPanel.setLayout(null);
		GridBagConstraints gbc_formPanel = new GridBagConstraints();
		gbc_formPanel.insets = new Insets(0, 0, 0, 0);
		gbc_formPanel.fill = GridBagConstraints.BOTH;
		gbc_formPanel.gridx = 0;
		gbc_formPanel.gridy = 0;
		add(formPanel, gbc_formPanel);

		JLabel lblNumberOfGames = new JLabel("Number of Games:");
		lblNumberOfGames.setBounds(10, 52, 130, 14);
		formPanel.add(lblNumberOfGames);

		JLabel lblDepth = new JLabel("Depth:");
		lblDepth.setBounds(10, 15, 47, 14);
		formPanel.add(lblDepth);

		rdbtnLow = new JRadioButton(String.valueOf(Constant.SIMULATED_START_DEPTH));
		buttonGroup.add(rdbtnLow);
		rdbtnLow.addActionListener(changeListener);
		rdbtnLow.setSelected(true);
		rdbtnLow.setBounds(110, 11, 47, 23);
		formPanel.add(rdbtnLow);

		rdbtnMedium = new JRadioButton(String.valueOf(Constant.SIMULATED_START_DEPTH + 2));
		buttonGroup.add(rdbtnMedium);
		rdbtnMedium.addActionListener(changeListener);
		rdbtnMedium.setBounds(166, 11, 52, 23);
		formPanel.add(rdbtnMedium);

		rdbtnHigh = new JRadioButton(String.valueOf(Constant.SIMULATED_START_DEPTH + 4));
		buttonGroup.add(rdbtnHigh);
		rdbtnHigh.addActionListener(changeListener);
		rdbtnHigh.setBounds(220, 11, 52, 23);
		formPanel.add(rdbtnHigh);

		rdbtnCustom = new JRadioButton("Custom");
		buttonGroup.add(rdbtnCustom);
		rdbtnCustom.addActionListener(changeListener);
		rdbtnCustom.setBounds(274, 11, 100, 23);
		formPanel.add(rdbtnCustom);

		numberTextField = new JTextField();
		numberTextField.setText("10");
		numberTextField.setBounds(134, 49, 67, 20);
		formPanel.add(numberTextField);
		numberTextField.setColumns(10);

		slider = new JSlider();
		slider.setMinimum(2);
		slider.setValueIsAdjusting(true);
		slider.setEnabled(false);
		slider.setPaintLabels(true);
		slider.setValue(2);
		slider.setMaximum(20);
		slider.setBounds(418, 11, 272, 20);
		slider.addChangeListener(sliderListener);
		formPanel.add(slider);

		btnStart = new JButton("Start");
		btnStart.setBounds(517, 48, 89, 23);
		btnStart.addActionListener(startHandler);
		formPanel.add(btnStart);

		JButton btnClear = new JButton("Clear");
		btnClear.setBounds(418, 48, 89, 23);
		btnClear.addActionListener(clearHandler);
		formPanel.add(btnClear);

		chckbxClearOnStart = new JCheckBox("Clear on start");
		chckbxClearOnStart.setSelected(true);
		chckbxClearOnStart.setBounds(301, 48, 111, 23);
		formPanel.add(chckbxClearOnStart);

		depthLabel = new JLabel(String.valueOf(Constant.SIMULATED_START_DEPTH));
		depthLabel.setBounds(60, 15, 35, 14);
		formPanel.add(depthLabel);
	}

	private void createEditorPanel() {
		JPanel editorPanel = new JPanel();
		editorPanel.setLayout(null);
		GridBagConstraints gbc_editorPanel = new GridBagConstraints();
		gbc_editorPanel.insets = new Insets(0, 0, 0, 0);
		gbc_editorPanel.fill = GridBagConstraints.BOTH;
		gbc_editorPanel.gridx = 0;
		gbc_editorPanel.gridy = 1;
		add(editorPanel, gbc_editorPanel);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setBounds(10, 11, 680, 377);
		editorPanel.add(scrollPane);

		editorPane = new JEditorPane();
		editorPane.setEditable(false);
		scrollPane.setViewportView(editorPane);
		editorPane.setText(editorText.toString());
	}

	private void createProgressPanel() {
		JPanel progressPanel = new JPanel();
		progressPanel.setLayout(null);
		GridBagConstraints gbc_progressPanel = new GridBagConstraints();
		gbc_progressPanel.fill = GridBagConstraints.BOTH;
		gbc_progressPanel.gridx = 0;
		gbc_progressPanel.gridy = 2;
		add(progressPanel, gbc_progressPanel);

		progressLabel = new JLabel("0%");
		progressLabel.setHorizontalAlignment(SwingConstants.CENTER);
		progressLabel.setBounds(10, 5, 581, 19);
		progressPanel.add(progressLabel);

		progressBar = new JProgressBar();
		progressBar.setForeground(Color.GREEN);
		progressBar.setBounds(10, 5, 581, 19);
		progressPanel.add(progressBar);

		btnCancel = new JButton("Cancel");
		btnCancel.setEnabled(false);
		btnCancel.setBounds(601, 3, 89, 23);
		btnCancel.addActionListener(cancelHandler);
		progressPanel.add(btnCancel);
	}

	private ActionListener cancelHandler = o -> {
		for (SimulatedTask task : taskList) {
			task.setStop(true);
			task = null;
		}
		editorText.append("Canceled Simulating Process!").append("\n");
		editorPane.setText(editorText.toString());
		toggleBtn(false);
	};

	private ActionListener changeListener = o -> {
		Enumeration<AbstractButton> list = buttonGroup.getElements();
		int start = Constant.SIMULATED_START_DEPTH;
		int index = -1;
		while (list.hasMoreElements()) {
			AbstractButton btn = list.nextElement();
			index++;
			if (btn.isSelected()) {
				break;
			}
		}
		if (index < 3) {
			slider.setEnabled(false);
			setDepth(start + index * 2);
			return;
		}
		slider.setEnabled(true);
		setDepth(slider.getValue());
	};

	private ChangeListener sliderListener = new ChangeListener() {

		@Override
		public void stateChanged(ChangeEvent e) {
			JSlider source = (JSlider) e.getSource();
			setDepth(source.getValue());
		}
	};

	private void toggleBtn(boolean start) {
		rdbtnLow.setEnabled(!start);
		rdbtnMedium.setEnabled(!start);
		rdbtnHigh.setEnabled(!start);
		rdbtnCustom.setEnabled(!start);
		if (!start && rdbtnCustom.isSelected())
			slider.setEnabled(!start);
		else if (start)
			slider.setEnabled(!start);
		btnStart.setEnabled(!start);
		numberTextField.setEnabled(!start);

		btnCancel.setEnabled(start);
	}

	private ActionListener startHandler = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent e) {
			String txt = numberTextField.getText();
			if (!StringUtils.isPositiveInteger(txt)) {
				try {
					JOptionPane.showMessageDialog(mainApp.getFrame(), "Please input a positive number!",
							"Invalid number string: " + txt, JOptionPane.WARNING_MESSAGE,
							new ImageIcon(new URL(STUFF_FOLDER, "10_50x50.png")));
				} catch (HeadlessException | MalformedURLException e1) {
					e1.printStackTrace();
				}
				return;
			}
			mainApp.setIngame(true);
			nWinGames = 0;
			nCompletedGames = 0;
			progressBar.setValue(0);
			progressLabel.setText("0%");
			progressPercent = 0;
			nGames = Integer.parseInt(txt);
			toggleBtn(true);
			if (chckbxClearOnStart.isSelected()) {
				clearConsole();
			}
			processMutex = new Semaphore(1);
			editorText.append("Start Simulating " + nGames + " Games...").append("\n");
			editorPane.setText(editorText.toString());
			executorService = Executors.newFixedThreadPool(MAX_THREAD_POOL);

			startTime = System.currentTimeMillis();
			taskList = new LinkedList<>();
			for (int i = 0; i < nGames; i++) {
				SimulatedTask task = new SimulatedTask(self);
				taskList.add(task);
				executorService.submit(task);
			}
		}
	};

	private ActionListener clearHandler = o -> clearConsole();

	private void clearConsole() {
		editorText = new StringBuilder();
		editorPane.setText(editorText.toString());
	}

	public void showResult(Board board) {
		++nCompletedGames;
		if (board == null) {
			editorText.append("Some errors has occured when calculating move in game " + nCompletedGames + ".");
		} else if (board.hasWon()) {
			nWinGames++;
			editorText.append("Game " + nCompletedGames + " has won - Score: " + board.getActualScore() + ".");
			try {
				dbMutex.acquire();

				String json = FileUtils.readFile(Constant.SAVE_FILE);
				HighScore[] arr = JsonUtils.fromJson(json, HighScore[].class);
				List<HighScore> listData = new ArrayList<>(Arrays.asList(arr));
				ListUtils.insert(listData, new HighScore("BetaG�u L" + depth, board.getActualScore()));
				FileUtils.writeFile(Constant.SAVE_FILE, JsonUtils.toJson(listData));

				dbMutex.release();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		} else {
			try {
				if (board.isTerminated()) {
					editorText.append("Game " + nCompletedGames + " has lost - Score: " + board.getActualScore() + ".");
				}
			} catch (CloneNotSupportedException e) {
				editorText.append("Some errors has occured when simulating game " + nCompletedGames + ".");
			}
		}
		editorText.append(" Won " + nWinGames + "/" + nCompletedGames + " games!").append("\n");
		editorPane.setText(editorText.toString());
		if (nCompletedGames == nGames) {
			Long currentTime = System.currentTimeMillis();
			Long deltaTime = currentTime - startTime;
			if (deltaTime > 60 * 1000) {
				long minute = deltaTime / 1000 / 60;
				long second = deltaTime / 1000 - 60 * minute;
				editorText.append("=======> Executing Time: " + minute + " Minutes " + second + " Seconds")
						.append("\n");
			} else {
				editorText.append(
						"=======> Executing Time: " + MathUtils.formatNumber(deltaTime * 1.0 / 1000) + " Seconds")
						.append("\n");
			}
			editorPane.setText(editorText.toString());
			toggleBtn(false);
			progressBar.setValue(100);
			progressLabel.setText("100%");
			mainApp.setIngame(false);
			editorText.append("=======> WIN RATE: " + MathUtils.formatNumber(nWinGames * 100.0 / nGames)).append("%\n");
			editorPane.setText(editorText.toString());
		}
	}

	public int getDepth() {
		return depth;
	}

	private void setDepth(int depth) {
		this.depth = depth;
		depthLabel.setText(String.valueOf(depth));
	}

	public void setMainApp(Application mainApp) {
		this.mainApp = mainApp;
	}

	public double getProgressPercent() {
		return progressPercent;
	}

	public void setProgressPercent(double progressPercent) {
		this.progressPercent = progressPercent;
	}

	public JProgressBar getProgressBar() {
		return progressBar;
	}

	public JLabel getProgressLabel() {
		return progressLabel;
	}

	public int getNumberOfGame() {
		return nGames;
	}

	public Semaphore getMutex() {
		return processMutex;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.chotoxautinh.game.view.component.CardPanel#onClosing()
	 */
	@Override
	public void closed() {
		if (taskList == null)
			return;
		for (SimulatedTask task : taskList) {
			task.setStop(true);
		}
	}

}

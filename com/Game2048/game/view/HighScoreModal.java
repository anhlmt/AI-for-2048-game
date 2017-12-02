package com.Game2048.game.view;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.SystemColor;
import java.awt.Toolkit;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

import com.Game2048.game.Application;
import com.Game2048.game.config.Constant;
import com.Game2048.game.model.HighScore;
import com.Game2048.util.FileUtils;
import com.Game2048.util.JsonUtils;
import com.Game2048.util.ListUtils;

public class HighScoreModal extends JDialog {

	private static final long serialVersionUID = -2874237291366345040L;
	private Application mainApp;
	private HighScore currentPoint;
	private List<HighScore> listData;
	private int index = Constant.NUMBER_HIGHSCORE;
	private JTable table;
	private String columnNames[] = { "No.", "Player Name", "Score" };
	private Object rowData[][] = new Object[10][3];

	public HighScoreModal(JFrame parent) {
		this(parent, null);
	}

	public HighScoreModal(JFrame parent, HighScore currentPoint) {
		super(parent);
		setCurrentPoint(currentPoint);
		initialize();
	}

	private void initialize() {
		setLayout();
		initComponent();
		pack();
		try {
			setIconImage(Toolkit.getDefaultToolkit().getImage(new URL(Constant.STUFF, "logo.jpg")));
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		setTitle("High Score");
		setLocationRelativeTo(null);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent we) {
				if (listData.size() > Constant.NUMBER_HIGHSCORE)
					listData.remove(Constant.NUMBER_HIGHSCORE);
				if (index < Constant.NUMBER_HIGHSCORE) {
					listData.get(index).setName((String) table.getValueAt(index, 1));
					FileUtils.writeFile(Constant.SAVE_FILE, JsonUtils.toJson(listData));
				}
				dispose();
			}
		});
	}

	private void setLayout() {
		getContentPane().setLayout(new BorderLayout(0, 0));
	}

	private void initComponent() {
		JLabel lblNewLabel = new JLabel("High Score");
		lblNewLabel.setBorder(new EmptyBorder(10, 0, 10, 0));
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 18));
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		getContentPane().add(lblNewLabel, BorderLayout.NORTH);

		loadData();
		getContentPane().add(table, BorderLayout.CENTER);

		JButton btnOKButton = new JButton("OK");
		btnOKButton.setBackground(SystemColor.textHighlight);
		btnOKButton.addActionListener(okHandler);
		getContentPane().add(btnOKButton, BorderLayout.SOUTH);
	}

	private void loadData() {
		String json = FileUtils.readFile(Constant.SAVE_FILE);
		HighScore[] arr = JsonUtils.fromJson(json, HighScore[].class);
		listData = new ArrayList<>(Arrays.asList(arr));
		if (currentPoint != null) {
			index = ListUtils.insert(listData, currentPoint);
		}

		int i = 0;
		for (HighScore score : listData) {
			if (i >= Constant.NUMBER_HIGHSCORE)
				break;
			rowData[i][0] = i + 1;
			rowData[i][1] = score.getName();
			rowData[i][2] = score.getScore();
			i++;
		}
		MyDefaultTableModel model = new MyDefaultTableModel(rowData, columnNames, 10, 3);
		table = new JTable(model);
		if (index < Constant.NUMBER_HIGHSCORE) {
			model.setCellEditable(index, 1, true);
			table.changeSelection(index, 1, false, false);
			table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
				public void valueChanged(ListSelectionEvent e) {
					if (!e.getValueIsAdjusting()) {
						table.changeSelection(index, 1, false, false);
					}
				}

			});
			table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			table.setColumnSelectionAllowed(true);
		}
		table.getColumnModel().getColumn(0).setPreferredWidth(30);
		table.getColumnModel().getColumn(1).setPreferredWidth(100);
	}

	private ActionListener okHandler = o -> dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));

	public Application getMainApp() {
		return mainApp;
	}

	public void setMainApp(Application mainApp) {
		this.mainApp = mainApp;
	}

	public HighScore getCurrentPoint() {
		return currentPoint;
	}

	public void setCurrentPoint(HighScore currentPoint) {
		this.currentPoint = currentPoint;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	private class MyDefaultTableModel extends DefaultTableModel {
		/**
		 * 
		 */
		private static final long serialVersionUID = -6045275012752659651L;
		private boolean[][] editable_cells; // 2d array to represent rows and
											// columns

		private MyDefaultTableModel(Object[][] rowData, String[] columnNames, int rows, int cols) { // constructor
			super(rowData, columnNames);
			this.editable_cells = new boolean[rows][cols];
		}

		@Override
		public boolean isCellEditable(int row, int column) { // custom
																// isCellEditable
																// function
			return this.editable_cells[row][column];
		}

		public void setCellEditable(int row, int col, boolean value) {
			this.editable_cells[row][col] = value; // set cell true/false
			this.fireTableCellUpdated(row, col);
		}
	}

}

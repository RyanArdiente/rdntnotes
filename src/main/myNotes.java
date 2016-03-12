package main;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;

public class myNotes extends JFrame
{
	private JButton closeButton;
	private JTextArea notesTextArea;
	private JScrollPane scrollPane;
	private JMenuItem openMenuItem;
	private JMenuItem saveMenuItem;
	private JMenuItem exitMenuItem;
	private JMenuItem changeStyleItem;
	private JMenuItem newMenuItem;
	private JMenuItem deleteMenuItem;
	private JMenuItem closeTabMenuItem;
	private JMenuItem closeAllTabMenuItem;
	private JTabbedPane filePane;
	private int windowCount = 1;
	private String defaultFileName = "Untitled";

	private JPanel jpan;
	private JLabel jlab;
	private JTextArea jtext;

	public myNotes()
	{
		super("RawkNote");
		addComponents();
		addMenu();
		addEventHandlers();
		setSize(500, 600);
		// pack();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);

	}

	private void addMenu()
	{
		JMenuBar menuBar = new JMenuBar();
		JMenu preferencesMenu = new JMenu("Preferences");
		JMenu fileMenu = new JMenu("File");
		openMenuItem = new JMenuItem("Open");
		saveMenuItem = new JMenuItem("Save");
		exitMenuItem = new JMenuItem("Exit");
		newMenuItem = new JMenuItem("New");
		closeTabMenuItem = new JMenuItem("Close Current Tab");
		closeAllTabMenuItem = new JMenuItem("Close All Tabs");

		changeStyleItem = new JMenuItem("Change Style");
		fileMenu.add(newMenuItem);
		fileMenu.add(openMenuItem);
		fileMenu.add(saveMenuItem);
		fileMenu.add(closeTabMenuItem);
		fileMenu.add(closeAllTabMenuItem);
		fileMenu.add(closeAllTabMenuItem);
		fileMenu.add(exitMenuItem);
		fileMenu.insertSeparator(5);
		menuBar.add(fileMenu);
		setJMenuBar(menuBar);

	}

	private void addEventHandlers()
	{
		class CloseListener implements ActionListener
		{
			public void actionPerformed(ActionEvent e)
			{
				dispose();
			}
		}
		class OpenListener implements ActionListener
		{
			public void actionPerformed(ActionEvent e)
			{
				JFileChooser fc = new JFileChooser(".");
				int result = fc.showOpenDialog(myNotes.this);
				if (result == JFileChooser.APPROVE_OPTION)
				{
					File openFile = fc.getSelectedFile();
					try
					{
						FileReader fileIn = new FileReader(openFile);
						char[] readBuffer = new char[(int) openFile.length()];
						fileIn.read(readBuffer);
						openTabbedPane(openFile.getName());
						int selectedIndex = filePane.getSelectedIndex();
						((LogTextPanel) filePane.getSelectedComponent()).append(new String(readBuffer));

						filePane.setTitleAt(selectedIndex, openFile.getName());
						fileIn.close();

					} catch (IOException ioe)
					{
						System.err.println("I/O Error on Open.");
					}
				}
			}

		}
		class SaveListener implements ActionListener
		{
			public void actionPerformed(ActionEvent e)
			{

				int selectedIndex = filePane.getSelectedIndex();

				String text = ((LogTextPanel) filePane.getSelectedComponent()).getText();

				JFileChooser fileChooser = new JFileChooser(".");
				fileChooser.setMultiSelectionEnabled(true);

				int result = fileChooser.showSaveDialog(myNotes.this);

				if (result == JFileChooser.APPROVE_OPTION)
				{
					File saveFile = fileChooser.getSelectedFile();
					try
					{
						FileWriter fileOut = new FileWriter(saveFile);
						fileOut.write(text);
						fileOut.close();
						filePane.setTitleAt(selectedIndex, saveFile.getName());
					} catch (IOException ioe)
					{
						System.err.println("I/O Error on Save.");
						JOptionPane.showMessageDialog(filePane, "Error on Save.");
					}
				}
			}
		}
		class NewListener implements ActionListener
		{
			public void actionPerformed(ActionEvent e)
			{
				newTabbedPane();
			}
		}
		class CloseCurrentTab implements ActionListener
		{
			public void actionPerformed(ActionEvent e)
			{
				if (filePane.getComponentCount() != 0)
				{
					int tabNum = filePane.getSelectedIndex();
					filePane.remove(filePane.getComponentAt(tabNum));
					windowCount--;
				} else
				{
					JOptionPane.showMessageDialog(filePane, "There are no open tabs!");
				}
			}
		}
		class CloseAllTabs implements ActionListener
		{
			public void actionPerformed(ActionEvent e)
			{
				if (filePane.getComponentCount() != 0)
				{
;
					int tabNum = filePane.getTabCount();

					filePane.removeAll();
					windowCount--;

				} else
				{
					JOptionPane.showMessageDialog(filePane, "There are no open tabs!");
				}
			}
		}

		ActionListener closeListener = new CloseListener();
		ActionListener saveMenuListener = new SaveListener();
		ActionListener openMenuListener = new OpenListener();
		ActionListener newListener = new NewListener();
		ActionListener closeCurrentTabListener = new CloseCurrentTab();
		ActionListener closeAllTabs = new CloseAllTabs();

		closeAllTabMenuItem.addActionListener(closeAllTabs);
		newMenuItem.addActionListener(newListener);
		closeTabMenuItem.addActionListener(closeCurrentTabListener);
		openMenuItem.addActionListener(openMenuListener);
		saveMenuItem.addActionListener(saveMenuListener);
		closeButton.addActionListener(closeListener);
		exitMenuItem.addActionListener(closeListener);
	}

	private void openTabbedPane(String name)
	{

		LogTextPanel panel = new LogTextPanel();
		filePane.addTab(name, panel);
		int num = filePane.indexOfTab(name);
		filePane.setSelectedIndex(num);

	}

	private void newTabbedPane()
	{

		LogTextPanel panel = new LogTextPanel();
		String name = defaultFileName + windowCount;
		filePane.addTab(defaultFileName + windowCount, panel);
		int num = filePane.indexOfTab(name);

		filePane.setSelectedIndex(num);

		windowCount++;
	}

	class LogTextPanel extends JPanel
	{

		private final JTextArea textArea;

		public LogTextPanel()
		{
			super(new GridLayout(1, 1));

			textArea = new JTextArea();
			add(textArea);
		}

		public void append(String text)
		{
			textArea.append(text);
		}

		public String getText()
		{
			return textArea.getText();
		}
	}

	private void addComponents()
	{

		filePane = new JTabbedPane();
		this.add(filePane);

		System.setProperty("apple.laf.useScreenMenuBar", "true");
		jpan = new JPanel();
		jlab = new JLabel("Filename:");
		jtext = new JTextArea("<Untitled>");
		// jtext = new JTextArea("Duhfuq");
		jtext.setEditable(false);
		// notesTextArea = new JTextArea(24, 60);
		// notesTextArea.setWrapStyleWord(true);
		// notesTextArea.setLineWrap(true);
		closeButton = new JButton("Close");
		// scrollPane = new JScrollPane(notesTextArea);
		Box southBox = new Box(BoxLayout.X_AXIS);

		jpan.add(jlab);
		jpan.add(jtext);
		jpan.add(closeButton);
		// southBox.add(jlab);
		// southBox.add(jtext);
		// southBox.add(closeButton);

		// this.add(jpan, BorderLayout.NORTH);
		// this.add(southBox, BorderLayout.SOUTH);
		// this.add(scrollPane, BorderLayout.CENTER);

	}

	public static void main(String[] args)
	{
		new myNotes();

	}

}

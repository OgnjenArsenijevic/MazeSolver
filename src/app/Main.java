package app;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.Dialog.ModalityType;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.Scanner;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class Main
{

	public static void main(String[] args)
	{
		try
		{
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}
		catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException e)
		{
		}
		int chosen = JOptionPane.showConfirmDialog(null, "Do you want to load predefined maze?", "",
				JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
		if (chosen == -1)
			return;
		int mat[][] = new int[100][100];
		for (int i = 0; i < 29; i++)
		{
			for (int j = 0; j < 29; j++)
			{
				if (((i & 1) == 0 && (j & 1) == 1) || ((i & 1) == 1 && (j & 1) == 0))
				{
					Random rand = new Random();
					mat[i][j] = rand.nextInt(101);
				}
				else
				{
					Random rand = new Random();
					mat[i][j] = (rand.nextInt(2) + 1) * (-1);
				}
			}
		}
		if (chosen == 0)
		{
			JDialog dialog = new JDialog();
			dialog.setLayout(new BorderLayout());
			dialog.setModalityType(ModalityType.APPLICATION_MODAL);
			dialog.setSize(300, 150);
			dialog.setLocationRelativeTo(null);
			JPanel panel = new JPanel();
			JLabel lbl = new JLabel("Type number between 1 and 10");
			JTextField tf = new JTextField();
			JButton btn = new JButton("Confirm");
			JPanel panel1 = new JPanel();
			tf.setPreferredSize(new Dimension(120, 20));
			panel.add(lbl);
			panel.add(tf);
			panel1.add(btn);
			btn.addActionListener(new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent e)
				{
					String s = tf.getText() + ".txt";
					File file = new File(s);
					Scanner sc;
					try
					{
						sc = new Scanner(file);
						for (int i = 0; i < 29; i++)
						{
							for (int j = 0; j < 29; j++)
								mat[i][j] = sc.nextInt();
						}
					}
					catch (FileNotFoundException e1)
					{
					}
					catch (Exception e2)
					{
					}
					dialog.dispose();
				}
			});
			dialog.add(panel, BorderLayout.CENTER);
			dialog.add(panel1, BorderLayout.SOUTH);
			dialog.setVisible(true);
		}
		JFrame frame = new JFrame("Maze solver");
		int screenHeight = 900;
		int screenWidth = 1700;
		frame.setSize(screenWidth, screenHeight);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.setLayout(new BorderLayout());
		JPanel jpanel = new JPanel(new GridLayout(29, 30));
		ImageIcon img = (ImageIcon) loadIcon("src/images/wall.png");
		ImageIcon img1 = (ImageIcon) loadIcon("src/images/robot.png");
		Font font = new Font("", 1, 9);
		JLabel lblScore = new JLabel("Score: ");
		lblScore.setFont(new Font("", 1, 12));
		ArrayList<JButton> list = new ArrayList<>();
		for (int i = 0; i < 29; i++)
		{
			if (i > 0)
				jpanel.add(new Label());
			else
			{
				JButton btn = new JButton(img1);
				btn.addActionListener(new ActionListener()
				{
					@Override
					public void actionPerformed(ActionEvent e)
					{
						for (int i = 0; i < list.size(); i++)
							list.get(i).setEnabled(false);
						File file = new File("file1.txt");
						try (FileWriter fw = new FileWriter(file, false);
								BufferedWriter bw = new BufferedWriter(fw);
								PrintWriter out = new PrintWriter(bw))
						{
							for (int i = 0; i < 29; i += 2)
							{
								for (int j = 0; j < 29; j += 2)
								{
									if (j + 2 < 29 && mat[i][j] == -2 && mat[i][j + 2] == -2)
										out.println((i / 2) + " " + (j / 2) + " " + (i / 2) + " " + (j / 2 + 1) + " "
												+ mat[i][j + 1]);
									if (i + 2 < 29 && mat[i][j] == -2 && mat[i + 2][j] == -2)
										out.println((i / 2) + " " + (j / 2) + " " + (i / 2 + 1) + " " + (j / 2) + " "
												+ mat[i + 1][j]);
								}
							}
						}
						catch (IOException ee)
						{
						}
						try
						{
							Process process = new ProcessBuilder("main.exe").start();
							process.waitFor();
						}
						catch (IOException e1)
						{
						}
						catch (InterruptedException e1)
						{
						}
						File file1 = new File("file2.txt");
						Thread thread = new Thread(new Runnable()
						{
							@Override
							public void run()
							{
								try
								{
									Scanner sc = new Scanner(file1);
									boolean shouldStop = false;
									while (sc.hasNextLine())
									{
										try
										{
											Thread.sleep(500);
											int ii = sc.nextInt();
											int jj = sc.nextInt();
											int w = sc.nextInt();
											if (ii == -1)
											{
												System.out.println(ii);
												shouldStop = true;
												JOptionPane.showMessageDialog(null, "Maze cannot be solved", "Error",
														JOptionPane.ERROR_MESSAGE);
												break;
											}
											list.get(ii * 2 * 29 + jj * 2).setIcon(img1);
											lblScore.setText("Score: " + w);
										}
										catch (NoSuchElementException e2)
										{
										}
										catch (InterruptedException e)
										{
										}
									}
									sc.close();
									if (shouldStop)
										return;
									list.get(841).setIcon(img1);
									list.get(841).setText("");
								}
								catch (FileNotFoundException e1)
								{
								}
							}

						});
						thread.start();
						btn.setEnabled(false);
					}
				});
				jpanel.add(btn);
			}
			for (int j = 0; j < 29; j++)
			{
				boolean check = true;
				JButton btn = new JButton();
				if (((i & 1) == 0 && (j & 1) == 1) || ((i & 1) == 1 && (j & 1) == 0))
				{
					// mat[i][j]=0;
					btn.setText(Integer.toString(mat[i][j]));
					btn.setFont(font);
				}
				else
				{
					if ((i & 1) == 0)
					{
						if (mat[i][j] == -1)
							btn.setIcon(img);
						// mat[i][j]=-1;
					}
					else
						check = false;
				}
				final int ii = i;
				final int jj = j;
				btn.addActionListener(new ActionListener()
				{
					@Override
					public void actionPerformed(ActionEvent e)
					{
						if (((ii & 1) == 0 && (jj & 1) == 1) || ((ii & 1) == 1 && (jj & 1) == 0))
						{
							JDialog jDialog = new JDialog();
							jDialog.setTitle("Enter value between 0 and 100");
							jDialog.setLayout(new BorderLayout());
							jDialog.setSize(300, 120);
							jDialog.setModalityType(ModalityType.APPLICATION_MODAL);
							jDialog.setLocationRelativeTo(frame);
							JLabel lbl = new JLabel("Value: ");
							JTextField jtf = new JTextField();
							jtf.setPreferredSize(new Dimension(80, 25));
							JPanel pan = new JPanel(new FlowLayout());
							pan.add(lbl);
							pan.add(jtf);
							JPanel pan1 = new JPanel(new FlowLayout());
							JButton btnConfirm = new JButton("Confirm");
							pan1.add(btnConfirm);
							jDialog.add(pan, BorderLayout.CENTER);
							jDialog.add(pan1, BorderLayout.SOUTH);
							btnConfirm.addActionListener(new ActionListener()
							{

								@Override
								public void actionPerformed(ActionEvent e)
								{
									int num = 0;
									boolean errOcurred = false;
									try
									{
										num = Integer.parseInt(jtf.getText());
									}
									catch (NumberFormatException e2)
									{
										errOcurred = true;
									}
									if (errOcurred || num < 0 || num > 100)
									{
										JOptionPane.showMessageDialog(null, "Enter valid number between 0 and 100",
												"Error", JOptionPane.ERROR_MESSAGE);
									}
									else
									{
										jDialog.dispose();
										btn.setText(Integer.toString(num));
										mat[ii][jj] = num;
									}

								}
							});
							jDialog.setVisible(true);
						}
						else
						{
							if (mat[ii][jj] == -1)
							{
								mat[ii][jj] = -2;
								btn.setIcon(null);
							}
							else
							{
								mat[ii][jj] = -1;
								btn.setIcon(img);
							}
						}
					}
				});
				list.add(btn);
				if (check)
				{
					jpanel.add(btn);
				}
				else
					jpanel.add(new Label());
			}
			if (i < 28)
				jpanel.add(new Label());
			else
			{
				JButton btn = new JButton();
				btn.setText("Exit");
				btn.setFont(font);
				list.add(btn);
				jpanel.add(btn);
			}
		}
		frame.add(jpanel, BorderLayout.CENTER);
		JPanel panel1 = new JPanel();
		panel1.add(lblScore);
		frame.add(panel1, BorderLayout.SOUTH);
		frame.setVisible(true);
	}

	public static Icon loadIcon(String fileName)
	{
		ImageIcon imageIcon = null;
		File file = new File(fileName);
		if (file.exists())
			imageIcon = new ImageIcon(fileName);
		else
			System.err.println("Resource not found: " + fileName);
		return imageIcon;
	}

}

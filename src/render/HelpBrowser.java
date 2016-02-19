/*
 *  FasT -- A Fast algorithm for simulaTions. 
 * 
 *  Copyright © 2016 Tourdetour
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *  
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *  
 *  Contact me by email : <TPEFasT@mail.com>
 */

package render; 
 
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.embed.swing.JFXPanel;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebEvent;
import javafx.scene.web.WebHistory;
import javafx.scene.web.WebView;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.plaf.basic.BasicArrowButton;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import static javafx.concurrent.Worker.State.FAILED;

/*
 * Forked from http://docs.oracle.com/javafx/2/swing/swing-fx-interoperability.htm#CHDIEEJE
 * Some help : http://stackoverflow.com/questions/18928333/how-to-program-back-and-forward-buttons-in-javafx-with-webview-and-webengine
 */

public class HelpBrowser extends JPanel {
 
	/**
	 * 
	 */
	private static final long serialVersionUID = 2680771439845250868L;
	private final JFXPanel jfxPanel = new JFXPanel();
    private WebEngine engine;
 
   // private final JPanel panel = new JPanel(new BorderLayout());
    private final JLabel lblStatus = new JLabel();


   // private final JButton btnGo = new JButton("Go");
  //  private final JTextField txtURL = new JTextField();
    private final JProgressBar progressBar = new JProgressBar();
    
 //   private ArrayList<String> history = new ArrayList<String>();
    ///private int index = 0;
    
    private final BasicArrowButton btnBack = new BasicArrowButton(SwingConstants.WEST);
    private final JButton btnForward = new BasicArrowButton(SwingConstants.EAST);
    
    private String homePage;
    private String title;
 
    public HelpBrowser() {
        super(new BorderLayout());
        initComponents();
    }
    
    public void setHomePage(String homePage)
    {
    	this.homePage=homePage;
    	this.loadURL(this.homePage);
    }

    
    private void initComponents() {
        createScene();
 
       /* ActionListener al = new ActionListener() {
            @Override 
            public void actionPerformed(ActionEvent e) {
                loadURL(txtURL.getText());
            }
        };*/
        
        ActionListener back = new ActionListener()
        		{

					@Override
					public void actionPerformed(ActionEvent e) {
						

					/*	if(index<=0)
						{
							return;
						}
						dontADD=-1;
						loadURL(history.get(index-1));
						System.out.print("Loading BackINDEX:"+(index-1)+"\n");*/
						goBack();
					}
        	
        		};

        		
             ActionListener forward = new ActionListener()
                		{

        					@Override
        					public void actionPerformed(ActionEvent e) {
        						/*if(history.size()<=index+1)
        						{
        							return;
        						}
        						dontADD=1;
        						loadURL(history.get(index+1));
        						System.out.print("Loading ForwardINDEX:"+(index+1)+"\n");*/
        						goForward();
        					}
                	
                		};

       // btnGo.addActionListener(al);
       // txtURL.addActionListener(al);
        btnForward.addActionListener(forward);
        btnBack.addActionListener(back);
        btnBack.setPreferredSize(new Dimension(100,20));
        btnForward.setPreferredSize(new Dimension(100,20));
  
        progressBar.setPreferredSize(new Dimension(150, 18));
        progressBar.setStringPainted(true);
  
        JPanel topBar = new JPanel(new FlowLayout(FlowLayout.LEFT,10,4));
        topBar.setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
      //  topBar.add(txtURL, BorderLayout.CENTER);
      //  topBar.add(btnGo, BorderLayout.EAST);
        topBar.add(btnBack);
        topBar.add(btnForward);
        
        JButton button = new JButton();
        try {
          Image img = ImageIO.read(getClass().getClassLoader().getResource("ressources/home-icon.png"));
          Image newimg = img.getScaledInstance(20, 20,  java.awt.Image.SCALE_SMOOTH ) ; 
          button.setIcon(new ImageIcon(newimg));
        } catch (IOException ex) {
        	ex.printStackTrace();
        }
        button.setPreferredSize(new Dimension(20,20));
        button.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				loadURL(homePage);
				
			}
        	
        });
        topBar.add(button);
 
        JPanel statusBar = new JPanel(new BorderLayout(5, 0));
        statusBar.setBorder(BorderFactory.createEmptyBorder(3, 5, 3, 5));
        statusBar.add(lblStatus, BorderLayout.CENTER);
        statusBar.add(progressBar, BorderLayout.EAST);
 
        this.add(topBar, BorderLayout.NORTH);
        this.add(jfxPanel, BorderLayout.CENTER);
        this.add(statusBar, BorderLayout.SOUTH);
        
      //  getContentPane().add(panel);
        
        setPreferredSize(new Dimension(1024, 600));
        //setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    //    this.pack();

    }
    
    public String goBack()
    {    
      final WebHistory history=engine.getHistory();
      ObservableList<WebHistory.Entry> entryList=history.getEntries();
      int currentIndex=history.getCurrentIndex();

      Platform.runLater(new Runnable() { public void run() { history.go(currentIndex>0?-1:0); } });
      return entryList.get(currentIndex>0?currentIndex-1:currentIndex).getUrl();
    }

    public String goForward()
    {    
      final WebHistory history=engine.getHistory();
      ObservableList<WebHistory.Entry> entryList=history.getEntries();
      int currentIndex=history.getCurrentIndex();

      Platform.runLater(new Runnable() { public void run() { history.go(currentIndex<entryList.size()-1?1:0); } });
      return entryList.get(currentIndex<entryList.size()-1?currentIndex+1:currentIndex).getUrl();
    }
 
    private void createScene() {
 
        Platform.runLater(new Runnable() {
            @Override 
            public void run() {
 
                WebView view = new WebView();
                engine = view.getEngine();
 
                engine.titleProperty().addListener(new ChangeListener<String>() {
                    @Override
                    public void changed(ObservableValue<? extends String> observable, String oldValue, final String newValue) {
                        SwingUtilities.invokeLater(new Runnable() {
                            @Override 
                            public void run() {
                            	title=newValue;
                              //  HelpBrowser.this.setTitle(newValue);
                            }
                        });
                    }
                });
 
                engine.setOnStatusChanged(new EventHandler<WebEvent<String>>() {
                    @Override 
                    public void handle(final WebEvent<String> event) {
                        SwingUtilities.invokeLater(new Runnable() {
                            @Override 
                            public void run() {
                                lblStatus.setText(event.getData());
                            }
                        });
                    }
                });
 
               engine.locationProperty().addListener(new ChangeListener<String>() {
                    @Override
                    public void changed(ObservableValue<? extends String> ov, String oldValue, final String newValue) {
                              //  txtURL.setText(newValue);
                    		/*	if(dontADD!=0)
                    			{
                    				System.out.print("We should not add");
                    				index+=dontADD;
                     				dontADD=0;
                    				return;

                    			}
                            	for(String s : history)
                            	{
                            		if(s==newValue)
                            			return;
                            	}
               
                            	history.add(newValue);
                            	index++;
                            	System.out.print("Index="+index+"\n");*/
                           
                       
                    }
                });
 
                engine.getLoadWorker().workDoneProperty().addListener(new ChangeListener<Number>() {
                    @Override
                    public void changed(ObservableValue<? extends Number> observableValue, Number oldValue, final Number newValue) {
                        SwingUtilities.invokeLater(new Runnable() {
                            @Override 
                            public void run() {
                                progressBar.setValue(newValue.intValue());
                            }
                        });
                    }
                });

                engine.getLoadWorker()
                        .exceptionProperty()
                        .addListener(new ChangeListener<Throwable>() {
 
                            public void changed(ObservableValue<? extends Throwable> o, Throwable old, final Throwable value) {
                                if (engine.getLoadWorker().getState() == FAILED) {
                                    SwingUtilities.invokeLater(new Runnable() {
                                        @Override public void run() {
                                            JOptionPane.showMessageDialog(
                                                    HelpBrowser.this,
                                                    (value != null) ?
                                                    engine.getLocation() + "\n" + value.getMessage() :
                                                    engine.getLocation() + "\nUnexpected error.",
                                                    "Loading error...",
                                                    JOptionPane.ERROR_MESSAGE);
                                        }
                                    });
                                }
                            }
                        });

                jfxPanel.setScene(new Scene(view));
            }
        });
    }
 
    private void loadURL(final String url) {
        Platform.runLater(new Runnable() {
            @Override 
            public void run() {
                String tmp = toURL(url);
 
                if (tmp == null) {
                    tmp = toURL("http://" + url);
                }
 
                engine.load(tmp);
            }
        });
    }

    private static String toURL(String str) {
        try {
            return new URL(str).toExternalForm();
        } catch (MalformedURLException exception) {
                return null;
        }
    }

  
}
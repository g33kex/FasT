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
 
import static javafx.concurrent.Worker.State.FAILED;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.plaf.basic.BasicArrowButton;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;

import game.FasT;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.concurrent.Worker;
import javafx.concurrent.Worker.State;
import javafx.embed.swing.JFXPanel;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebEvent;
import javafx.scene.web.WebHistory;
import javafx.scene.web.WebView;

/*
 * Forked from http://docs.oracle.com/javafx/2/swing/swing-fx-interoperability.htm#CHDIEEJE
 * Some help : http://stackoverflow.com/questions/18928333/how-to-program-back-and-forward-buttons-in-javafx-with-webview-and-webengine
 */

public class HelpBrowser extends JPanel {
 
	/**
	 * 
	 */
	private static final long serialVersionUID = 5221597464166536291L;

	public boolean fullBrowser = false; 
	
	private final JFXPanel jfxPanel = new JFXPanel();
    private WebEngine engine;
    WebView view; 
 
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
  
        if(!this.fullBrowser)
        {
        	btnForward.setVisible(false);
        	btnBack.setVisible(false);
        }
        
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
 
                view = new WebView();
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

                
               engine.getLoadWorker().stateProperty().addListener(new ChangeListener<State>() {
                    public void changed(ObservableValue ov, State oldState, State newState) {
                        if (newState == Worker.State.SUCCEEDED) {
                                // note next classes are from org.w3c.dom domain
                            EventListener listener = new EventListener() {
								@Override
								public void handleEvent(org.w3c.dom.events.Event evt) {
									String link = "/website/"+((Element)evt.getTarget()).getAttribute("href");
									FasT.getFasT().getLogger().debug("Trying to open url = "+link);
									loadURL(link);
									//FasT.getFasT().getLogger().debug("Click on a link !");
									//FasT.getFasT().getLogger().debug(((Element)evt.getTarget()).getAttribute("href"));
								}
                            };

                           Document doc = engine.getDocument();
                            //Element el = doc.getElementById("a");
                            NodeList list = doc.getElementsByTagName("a");
                            
                            
                            for (int i=0; i<list.getLength(); i++)
                                ((EventTarget)list.item(i)).addEventListener("click", listener, false);
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
                    tmp = toURL("file://" + url);
                }
                
        
               // FasT.getFasT().getLogger().debug("Trying to load url : "+tmp);
                FasT.getFasT().getLogger().debug("Trying to open url " + url);
               try {
            	  /*Path p = Paths.get(this.getClass().getResource(url).toURI());
            	   

            	// final Map<String, ?> env = Collections.emptyMap();
                 final FileSystem fs = FileSystems.newFileSystem(p, this.getClass().getClassLoader());
              //   final Path path = fs.provider().getPath(uri);*/
            	  
            	   
            	   InputStream is = this.getClass().getResourceAsStream(url);
            	   
            	   String content = readStream(is);

                 FasT.getFasT().getLogger().debug("Loading url : "+url);
				engine.loadContent(content);
				
               
               } catch (IOException e) {
				// TODO Auto-generated catch block
				
				FasT.getFasT().getLogger().error("Cannot load url : "+url);
				//e.printStackTrace();
			}
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

    public static String readStream(InputStream stream) throws IOException
    {
    	  StringBuilder inputStringBuilder = new StringBuilder();
          BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(stream, "UTF-8"));
          String line = bufferedReader.readLine();
          while(line != null){
              inputStringBuilder.append(line);inputStringBuilder.append('\n');
              line = bufferedReader.readLine();
          }
          return inputStringBuilder.toString();
    }
    
  
}
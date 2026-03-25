/*
    File name:  GCESports_GUI.java
    Purpose:    Runs the GUI JFrame app
    Author:     Mark W
    Date:       25-03-2026
    Version:    1.0
    NOTES:      AT2 Development of a functional application that lists / updates competitions and teams.
    
    Required functionalities:
    1.  APPLICATION IS LAUNCHED - Done.
    1.1 Read-in competition results from competitions.csv external file and set up in ArrayList<Competition> competitions - Done.
    1.2 Read-in team names and details from teams.csv external file and set up in ArrayList<Team> teams - Done.
    1.3 Display read-in competition results in JTable - Done.
    1.4 Display read-in team names in 2 x JComboBoxes - Done.
    1.5 Display team info for first team name in Update panel - Done.

    2.  ADD A NEW COMPETITION RESULT
    2.1 Add a new (validated) competition result to ArrayList<Competition> competitions
        and display in JTable - Done.

    3.  ADD A NEW TEAM
    3.1 Add a new (validated) team to ArrayList<Team> teams
        and add to the 2 x JComboBoxes - Done.

    4.  UPDATE AN EXISTING TEAM
    4.1 Update details for a selected (existing) team - Done.
        Validate changes to person, phone, email and player names
        and change values in ArrayList<Team> for the specific team - Done.

    5.  DISPLAY TOP TEAMS WITH LEADERBOARD
    5.1 Calculate total points earned for each team in ArrayList<Team> teams - Done.
    5.2 List the teams and the total points in total points descending order (highest to lowest) - Done.

    6.  APPLICATION IS CLOSED AND SAVING OF DATA
    6.1 When application is closed, provide option for user
        to save changes (competitions, teams) - Done.
    6.2 Save (write) to 2 x external csv files the data from:
        ArrayList<Competition> competitions - Done.
        ArrayList<Team> teams - Done.
*/
package gcesports_gui;

import java.util.List;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

public class GCESports_GUI extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(GCESports_GUI.class.getName());
    
    private ArrayList<Competition> competitionList;
    private ArrayList<Team> teamList;
    
    // These 2 variables are for Top Teams
    private Map<String, Integer> tally = new HashMap<>();
    private List<Team> leaderboard = new ArrayList<>();
    
    private boolean comboBoxStatus = false;
    
    // Declare
    private DefaultTableModel compResultsTableModel;

    public GCESports_GUI() {

        // ***** INITIATE 2 ARRAY LISTS **********
        competitionList = new ArrayList<Competition>();
        teamList = new ArrayList<Team>();
        
        // Instantiate
        compResultsTableModel = new DefaultTableModel();
        
        
        // CUSTOM TABLE MODEL
        String[] columnNames_Results = new String[] {"Date", "Location", "Game", "Team", "Points"};
        compResultsTableModel.setColumnIdentifiers(columnNames_Results);
        
        // ***** SWING COMPONENTS *********
        initComponents();
        
        readCompetitionData();
        readTeamData();
        resizeTableColumns();
        displayCompetitions();
        displayTeams();
        
        comboBoxStatus = true;
    }

    
    /**
     *  Sets up customised widths for the competitions JTable.
     *     
     * @return 
     */
    private void resizeTableColumns() {
        // Columns: Date, Location, Competition, Platform, Team, Points
        // (total numeric values must = 1)
        double[] columnWidthPercentage = {0.2f, 0.2f, 0.3f, 0.2f, 0.1f};
        int tableWidth = compResults_JTable.getWidth();
        TableColumn column;
        TableColumnModel tableColumnModel = compResults_JTable.getColumnModel();
        int cantCols = tableColumnModel.getColumnCount();
        for (int i = 0; i < cantCols; i++) {
            column = tableColumnModel.getColumn(i);
            float pWidth = Math.round(columnWidthPercentage[i] * tableWidth);
            column.setPreferredWidth((int)pWidth);
        }
    }
    
    /**
     * Read in competition data from "competitions.csv" - 
     * add it to competitionList (type:ArrayList).
     * Loop through the Map (tally) and check for existing key - 
     * add if not found, update score if found. 
     *     
     * @return 
     */
    private void readCompetitionData() {
        
        try {
           FileReader reader = new FileReader("competitions.csv");
           BufferedReader bufferedReader = new BufferedReader(reader);
           String line;
        
           while ((line = bufferedReader.readLine()) != null) {
               if (line.length() > 0) {
                   String[] lineArray = line.split(",");
                   String game = lineArray[0];
                   String location = lineArray[1];
                   String date = lineArray[2];
                   String team = lineArray[3];
                   int points = Integer.parseInt(lineArray[4]);
                   
                   Competition comp = new Competition(game, location, date, team, points);
                   competitionList.add(comp);
                   
                   if (tally.containsKey(team)) {
                        int currentScore = tally.get(team);
                        tally.put(team, currentScore + points);
                    } else {
                        tally.put(team, points);
                    }  
               }   
           }
           reader.close();
        
           // looping through Map for displaying the top 5 teams
           // put them into a Team object
           // add them to leaderboard list
           for (Map.Entry<String, Integer> entry : tally.entrySet()) {
               // We turn each Map entry into a "Team" object
               Team team = new Team(entry.getKey(), entry.getValue());
               leaderboard.add(team);
           }

            // STEP 3: Use the Collections to sort the list
            // It looks at the "compareTo" rule inside the Team class
           Collections.sort(leaderboard);
        }
        catch (FileNotFoundException fnfe) {
           System.out.println("ERROR: 'competitions.csv' file not found!"); 
        }
        catch (IOException ioe) {
            System.out.println("ERROR: 'competitions.csv' file found, but there is a read problem!");
        }
        catch (NumberFormatException ex) {
            System.out.println("ERROR: Exception caught - check code!");
        }      
    }
    
    /**
     * Read in the team data from "teams.csv" file.
     * Add the data into teamList (type:ArrayList).
     *     
     * @return 
     */
    private void readTeamData() {

        try {
           FileReader reader = new FileReader("teams.csv");
           BufferedReader bufferedReader = new BufferedReader(reader);
           String line;
           
           while ((line = bufferedReader.readLine()) != null) {
               if (line.length() > 0) {
                   String[] lineArray = line.split(",");
                   String teamName = lineArray[0];
                   String contactPerson = lineArray[1];
                   String contactPhone = lineArray[2];
                   String contactEmail = lineArray[3];
                   
                   Team team = new Team(teamName, contactPerson, contactPhone, contactEmail);
                   teamList.add(team);
               }         
           }  
           reader.close();
        }
        catch (FileNotFoundException fnfe) {
           System.out.println("ERROR: 'teams.csv' file not found!"); 
        }
        catch (IOException ioe) {
            System.out.println("ERROR: 'teams.csv' file found, but there is a read problem!");
        }
        catch (NumberFormatException ex) {
            System.out.println("ERROR: Exception caught - check code!");
        }
    }
    
    /**
     * Display the competition information in the table for competition results.
     *     
     * @return 
     */
    private void displayCompetitions() {
        // populate competition data into JTable
        if (competitionList.size() > 0) {
            // create Object[] 2D array for JTable
            Object[][] competitions2DArray = new Object[competitionList.size()][];
            // populate 2D array from competitions ArrayList
            for (int i = 0; i < competitionList.size(); i++) {
                // create Object[] for single row of data containing 6 components
                Object[] competition = new Object[5];
                // game
                competition[2] = competitionList.get(i).getCompetitionGame();
                // location
                competition[1] = competitionList.get(i).getCompetitionLocation();
                // date
                competition[0] = competitionList.get(i).getCompetitionDate();
                // team
                competition[3] = competitionList.get(i).getCompetitionTeam();
                // points
                competition[4] = competitionList.get(i).getCompetitionPoints();
                // append to 2D array
                competitions2DArray[i] = competition;
            }

            // first, remove all existing rows if there are any
            if (compResultsTableModel.getRowCount() > 0) {
                for (int i = compResultsTableModel.getRowCount() - 1; i > -1; i--) {
                    compResultsTableModel.removeRow(i);
                }
            }

            // next, put new set of row data
            if (competitions2DArray.length > 0) {
                // add data to tableModel
                for (int row = 0; row < competitions2DArray.length; row++) {
                    compResultsTableModel.addRow(competitions2DArray[row]);
                }
            // update
            compResultsTableModel.fireTableDataChanged();
            }	
        }
    }
    
    /**
     * Populate the comboBox with the team names.
     * Clear everything out first, to ensure it contains only the updated values.
     *     
     * @return 
     */
    private void displayTeams() {
        if (selectTeam_JComboBox.getItemCount() > 0) {
            selectTeam_JComboBox.removeAllItems();
        }
        
        if (updateTeam_JComboBox.getItemCount() > 0) {
            updateTeam_JComboBox.removeAllItems();
        }     
        
        if (teamList.size() > 0) {
            for (int i = 0; i < teamList.size(); i++) {
                selectTeam_JComboBox.addItem(teamList.get(i).getTeamName());
                updateTeam_JComboBox.addItem(teamList.get(i).getTeamName()); 
                //IO.println(teamList.get(i).getTeamName());
            }
        }
    }
    
    /**
     * Displays the selected teams contact details.
     * Textfields are populated based on the index of the comboBox.
     *     
     * @return 
     */
    private void displayTeamDetails() {
        
        int itemIndexSelected = 0;
        if (comboBoxStatus == true) {
            itemIndexSelected = updateTeam_JComboBox.getSelectedIndex();
            if (itemIndexSelected < 0) {
                itemIndexSelected = 0;
            }
        }       
        
        // figuring out how to fill in the other textfields with individual bits of information!
        uContactPerson_JTextField.setText(teamList.get(itemIndexSelected).getContactPerson());
        uContactEm_JTextField.setText(teamList.get(itemIndexSelected).getContactEmail());
        uContactPh_JTextField.setText(teamList.get(itemIndexSelected).getContactPhone());
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        header_JPanel = new javax.swing.JPanel();
        image_JLabel = new javax.swing.JLabel();
        body_JPanel = new javax.swing.JPanel();
        content_JTabbedPanel = new javax.swing.JTabbedPane();
        result_JPanel = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        compResults_JTable = new javax.swing.JTable();
        topTeams_JButton = new javax.swing.JButton();
        resultHeading_JLabel = new javax.swing.JLabel();
        addResult_JPanel = new javax.swing.JPanel();
        addNewResult_JLabel = new javax.swing.JLabel();
        addDate_JTextField = new javax.swing.JTextField();
        addLocation_JTextField = new javax.swing.JTextField();
        addGame_JTextField = new javax.swing.JTextField();
        selectTeam_JComboBox = new javax.swing.JComboBox<>();
        addPoints_JTextField = new javax.swing.JTextField();
        addresult_JButton = new javax.swing.JButton();
        addDate_JLabel = new javax.swing.JLabel();
        addLocation_JLabel = new javax.swing.JLabel();
        addGame_JLabel = new javax.swing.JLabel();
        addTeam_JLabel = new javax.swing.JLabel();
        addPoints_JLabel = new javax.swing.JLabel();
        addTeam_JPanel = new javax.swing.JPanel();
        teamName_JTextField = new javax.swing.JTextField();
        contactPerson_JTextField = new javax.swing.JTextField();
        contactPh_JTextField = new javax.swing.JTextField();
        contactEm_JTextField = new javax.swing.JTextField();
        teamName_JLabel = new javax.swing.JLabel();
        contactPerson_JLabel = new javax.swing.JLabel();
        contactPh_JLabel = new javax.swing.JLabel();
        contactEm_JLabel = new javax.swing.JLabel();
        addHeading_JLabel = new javax.swing.JLabel();
        addNewTeam_JButton = new javax.swing.JButton();
        updateTeam_JPanel = new javax.swing.JPanel();
        updateTeam_JComboBox = new javax.swing.JComboBox<>();
        uContactPerson_JTextField = new javax.swing.JTextField();
        uContactPh_JTextField = new javax.swing.JTextField();
        uContactEm_JTextField = new javax.swing.JTextField();
        team_JLabel = new javax.swing.JLabel();
        uContactPerson_JLabel = new javax.swing.JLabel();
        uContactPh_JLabel = new javax.swing.JLabel();
        uContactEm_JLabel = new javax.swing.JLabel();
        updateTeam_JButton = new javax.swing.JButton();
        uTeamHeading_JLabel = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Gold Coast E-Sports Competition Data");
        setBackground(new java.awt.Color(255, 255, 255));
        setPreferredSize(new java.awt.Dimension(800, 800));
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        header_JPanel.setBackground(new java.awt.Color(255, 255, 255));
        header_JPanel.setForeground(new java.awt.Color(255, 255, 255));
        header_JPanel.setAlignmentX(0.0F);
        header_JPanel.setAlignmentY(0.0F);
        header_JPanel.setRequestFocusEnabled(false);

        image_JLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image_package/img/GoldCoastESports_Header.jpg"))); // NOI18N
        image_JLabel.setPreferredSize(null);

        javax.swing.GroupLayout header_JPanelLayout = new javax.swing.GroupLayout(header_JPanel);
        header_JPanel.setLayout(header_JPanelLayout);
        header_JPanelLayout.setHorizontalGroup(
            header_JPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(image_JLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        header_JPanelLayout.setVerticalGroup(
            header_JPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(image_JLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        body_JPanel.setBackground(new java.awt.Color(255, 255, 255));
        body_JPanel.setForeground(new java.awt.Color(255, 255, 255));
        body_JPanel.setAlignmentX(0.0F);
        body_JPanel.setAlignmentY(0.0F);
        body_JPanel.setPreferredSize(new java.awt.Dimension(800, 650));

        content_JTabbedPanel.setPreferredSize(new java.awt.Dimension(800, 1200));

        result_JPanel.setBackground(new java.awt.Color(255, 255, 255));

        compResults_JTable.setModel(compResultsTableModel);
        jScrollPane1.setViewportView(compResults_JTable);

        topTeams_JButton.setText("Display Top Teams");
        topTeams_JButton.addActionListener(this::topTeams_JButtonActionPerformed);

        resultHeading_JLabel.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        resultHeading_JLabel.setText("Team Competition Results");

        javax.swing.GroupLayout result_JPanelLayout = new javax.swing.GroupLayout(result_JPanel);
        result_JPanel.setLayout(result_JPanelLayout);
        result_JPanelLayout.setHorizontalGroup(
            result_JPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(result_JPanelLayout.createSequentialGroup()
                .addGap(31, 31, 31)
                .addGroup(result_JPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(resultHeading_JLabel)
                    .addGroup(result_JPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(topTeams_JButton)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 725, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(42, Short.MAX_VALUE))
        );
        result_JPanelLayout.setVerticalGroup(
            result_JPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(result_JPanelLayout.createSequentialGroup()
                .addGap(50, 50, 50)
                .addComponent(resultHeading_JLabel)
                .addGap(38, 38, 38)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 355, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(37, 37, 37)
                .addComponent(topTeams_JButton)
                .addContainerGap(80, Short.MAX_VALUE))
        );

        content_JTabbedPanel.addTab("Team Competition Result", result_JPanel);

        addResult_JPanel.setBackground(new java.awt.Color(255, 255, 255));

        addNewResult_JLabel.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        addNewResult_JLabel.setText("Add New Competition Result");

        selectTeam_JComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        addresult_JButton.setText("Add New Competition Result");
        addresult_JButton.addActionListener(this::addresult_JButtonActionPerformed);

        addDate_JLabel.setText("Date");

        addLocation_JLabel.setText("Location");

        addGame_JLabel.setText("Game");

        addTeam_JLabel.setText("Team");

        addPoints_JLabel.setText("Points");

        javax.swing.GroupLayout addResult_JPanelLayout = new javax.swing.GroupLayout(addResult_JPanel);
        addResult_JPanel.setLayout(addResult_JPanelLayout);
        addResult_JPanelLayout.setHorizontalGroup(
            addResult_JPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(addResult_JPanelLayout.createSequentialGroup()
                .addGap(73, 73, 73)
                .addComponent(addNewResult_JLabel)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, addResult_JPanelLayout.createSequentialGroup()
                .addContainerGap(224, Short.MAX_VALUE)
                .addGroup(addResult_JPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(addDate_JLabel, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(addLocation_JLabel, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(addGame_JLabel, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(addTeam_JLabel, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(addPoints_JLabel, javax.swing.GroupLayout.Alignment.TRAILING))
                .addGap(58, 58, 58)
                .addGroup(addResult_JPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(addPoints_JTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(addResult_JPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(addresult_JButton)
                        .addGroup(addResult_JPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(addDate_JTextField)
                            .addComponent(addLocation_JTextField)
                            .addComponent(addGame_JTextField)
                            .addComponent(selectTeam_JComboBox, 0, 250, Short.MAX_VALUE))))
                .addGap(220, 220, 220))
        );
        addResult_JPanelLayout.setVerticalGroup(
            addResult_JPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(addResult_JPanelLayout.createSequentialGroup()
                .addGap(60, 60, 60)
                .addComponent(addNewResult_JLabel)
                .addGap(86, 86, 86)
                .addGroup(addResult_JPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(addDate_JTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(addDate_JLabel))
                .addGap(42, 42, 42)
                .addGroup(addResult_JPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(addLocation_JTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(addLocation_JLabel))
                .addGap(39, 39, 39)
                .addGroup(addResult_JPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(addGame_JTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(addGame_JLabel))
                .addGap(41, 41, 41)
                .addGroup(addResult_JPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(selectTeam_JComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(addTeam_JLabel))
                .addGap(42, 42, 42)
                .addGroup(addResult_JPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(addPoints_JTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(addPoints_JLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 51, Short.MAX_VALUE)
                .addComponent(addresult_JButton)
                .addGap(89, 89, 89))
        );

        content_JTabbedPanel.addTab("Add New Competition Result", addResult_JPanel);

        addTeam_JPanel.setBackground(new java.awt.Color(255, 255, 255));

        teamName_JTextField.setBackground(new java.awt.Color(255, 255, 255));

        contactPerson_JTextField.setBackground(new java.awt.Color(255, 255, 255));

        contactPh_JTextField.setBackground(new java.awt.Color(255, 255, 255));

        contactEm_JTextField.setBackground(new java.awt.Color(255, 255, 255));

        teamName_JLabel.setText("Team Name");

        contactPerson_JLabel.setText("Contact Person");

        contactPh_JLabel.setText("Contact Phone");

        contactEm_JLabel.setText("Contact Email");

        addHeading_JLabel.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        addHeading_JLabel.setText("Add New Team");

        addNewTeam_JButton.setText("Add New Team");
        addNewTeam_JButton.addActionListener(this::addNewTeam_JButtonActionPerformed);

        javax.swing.GroupLayout addTeam_JPanelLayout = new javax.swing.GroupLayout(addTeam_JPanel);
        addTeam_JPanel.setLayout(addTeam_JPanelLayout);
        addTeam_JPanelLayout.setHorizontalGroup(
            addTeam_JPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(addTeam_JPanelLayout.createSequentialGroup()
                .addGap(96, 96, 96)
                .addGroup(addTeam_JPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(addNewTeam_JButton)
                    .addGroup(addTeam_JPanelLayout.createSequentialGroup()
                        .addGroup(addTeam_JPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(addHeading_JLabel)
                            .addComponent(contactEm_JLabel)
                            .addComponent(contactPh_JLabel)
                            .addComponent(contactPerson_JLabel)
                            .addComponent(teamName_JLabel))
                        .addGap(49, 49, 49)
                        .addGroup(addTeam_JPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(contactEm_JTextField)
                            .addComponent(contactPh_JTextField)
                            .addComponent(contactPerson_JTextField)
                            .addComponent(teamName_JTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(229, Short.MAX_VALUE))
        );
        addTeam_JPanelLayout.setVerticalGroup(
            addTeam_JPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(addTeam_JPanelLayout.createSequentialGroup()
                .addGap(78, 78, 78)
                .addComponent(addHeading_JLabel)
                .addGap(70, 70, 70)
                .addGroup(addTeam_JPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(teamName_JLabel)
                    .addComponent(teamName_JTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(32, 32, 32)
                .addGroup(addTeam_JPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(contactPerson_JLabel)
                    .addComponent(contactPerson_JTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(42, 42, 42)
                .addGroup(addTeam_JPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(contactPh_JLabel)
                    .addComponent(contactPh_JTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(37, 37, 37)
                .addGroup(addTeam_JPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(contactEm_JLabel)
                    .addComponent(contactEm_JTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(42, 42, 42)
                .addComponent(addNewTeam_JButton)
                .addContainerGap(171, Short.MAX_VALUE))
        );

        content_JTabbedPanel.addTab("Add New Team", addTeam_JPanel);

        updateTeam_JPanel.setBackground(new java.awt.Color(255, 255, 255));

        updateTeam_JComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        updateTeam_JComboBox.addItemListener(this::updateTeam_JComboBoxItemStateChanged);

        team_JLabel.setText("Team");

        uContactPerson_JLabel.setText("Contact Person");

        uContactPh_JLabel.setText("Contact Phone");

        uContactEm_JLabel.setText("Contact Email");

        updateTeam_JButton.setText("Update Existing Team");
        updateTeam_JButton.addActionListener(this::updateTeam_JButtonActionPerformed);

        uTeamHeading_JLabel.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        uTeamHeading_JLabel.setText("Update Existing Team");

        javax.swing.GroupLayout updateTeam_JPanelLayout = new javax.swing.GroupLayout(updateTeam_JPanel);
        updateTeam_JPanel.setLayout(updateTeam_JPanelLayout);
        updateTeam_JPanelLayout.setHorizontalGroup(
            updateTeam_JPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, updateTeam_JPanelLayout.createSequentialGroup()
                .addContainerGap(161, Short.MAX_VALUE)
                .addGroup(updateTeam_JPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(updateTeam_JButton)
                    .addGroup(updateTeam_JPanelLayout.createSequentialGroup()
                        .addGroup(updateTeam_JPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(team_JLabel, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(uContactPerson_JLabel, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(uContactPh_JLabel, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(uContactEm_JLabel, javax.swing.GroupLayout.Alignment.TRAILING))
                        .addGap(43, 43, 43)
                        .addGroup(updateTeam_JPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(uContactPerson_JTextField, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(uContactPh_JTextField, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(uContactEm_JTextField, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(updateTeam_JComboBox, 0, 250, Short.MAX_VALUE))))
                .addGap(263, 263, 263))
            .addGroup(updateTeam_JPanelLayout.createSequentialGroup()
                .addGap(98, 98, 98)
                .addComponent(uTeamHeading_JLabel)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        updateTeam_JPanelLayout.setVerticalGroup(
            updateTeam_JPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(updateTeam_JPanelLayout.createSequentialGroup()
                .addGap(68, 68, 68)
                .addComponent(uTeamHeading_JLabel)
                .addGap(92, 92, 92)
                .addGroup(updateTeam_JPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(updateTeam_JComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(team_JLabel))
                .addGap(43, 43, 43)
                .addGroup(updateTeam_JPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(uContactPerson_JTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(uContactPerson_JLabel))
                .addGap(44, 44, 44)
                .addGroup(updateTeam_JPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(uContactPh_JTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(uContactPh_JLabel))
                .addGap(46, 46, 46)
                .addGroup(updateTeam_JPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(uContactEm_JTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(uContactEm_JLabel))
                .addGap(48, 48, 48)
                .addComponent(updateTeam_JButton)
                .addContainerGap(131, Short.MAX_VALUE))
        );

        content_JTabbedPanel.addTab("Update Existing Team", updateTeam_JPanel);

        javax.swing.GroupLayout body_JPanelLayout = new javax.swing.GroupLayout(body_JPanel);
        body_JPanel.setLayout(body_JPanelLayout);
        body_JPanelLayout.setHorizontalGroup(
            body_JPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(content_JTabbedPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
        );
        body_JPanelLayout.setVerticalGroup(
            body_JPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(body_JPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(content_JTabbedPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 650, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(header_JPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(body_JPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 798, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(header_JPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(body_JPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 656, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Updates the teams comboBox when state changes by calling the method
     * displayTeamDetails().
     *     
     * @param evt the event that occurred
     * @return
     */ 
    private void updateTeam_JComboBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_updateTeam_JComboBoxItemStateChanged
        displayTeamDetails();
    }//GEN-LAST:event_updateTeam_JComboBoxItemStateChanged

    
    /**
     * Adds a new team when the "add new team" button is clicked in the GUI.
     * Runs the validation method to check for errors.
     * Shows a dialog box to confirm "add new team".
     *     
     * @param evt the event that occurred
     * @return
     */
    private void addNewTeam_JButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addNewTeam_JButtonActionPerformed
        // get form fields
        String addTeam = teamName_JTextField.getText();
        String addPerson = contactPerson_JTextField.getText();
        String addPhone = contactPh_JTextField.getText();
        String addEmail = contactEm_JTextField.getText();
    
        // validate
        Boolean valid = validateNewTeam(addTeam, addPerson, addPhone, addEmail);
        if (valid) {
           
            // showDialog
            int yesOrNo = JOptionPane.showConfirmDialog(null, "You are about to enter a new team: " + addTeam, "Save Changes?", JOptionPane.YES_NO_OPTION);
            
            if (yesOrNo == JOptionPane.YES_OPTION) {
                Team team = new Team(addTeam, addPerson, addPhone, addEmail);
                teamList.add(team);    
                displayTeams(); 
            }       
        }
    }//GEN-LAST:event_addNewTeam_JButtonActionPerformed

    /**
     * Check the data entered into the form controls for a new team is valid.
     *     
     * @param team      Name of the team.
     * @param person    Team contact person.
     * @param phone     Contact persons phone number.
     * @param email     Contact persons email.
     * @return true if all form fields are valid, false if any field is left blank.
     */
    public Boolean validateNewTeam(String team, String person, String phone, String email) {

        Boolean isValid = true;
        String errorMsg = "Error(s) were detected: \n";
        
        // validate Team
        if (team.isEmpty()) {
            errorMsg += "Team Name is not valid. Cannot be blank. \n";
            isValid = false;
        }
        
        // validate Contact Person
        if (person.isEmpty()) {
            errorMsg += "Contact Person is not valid. Cannot be blank. \n";
            isValid = false;
        }

        // validate Contact Phone
        if (phone.isEmpty()) {
            errorMsg += "Contact Phone is not valid. Cannot be blank. \n";
            isValid = false;
        }        
        
        // validate Contact Email
        if (email.isEmpty()) {
            errorMsg += "Email address is not valid. Cannot be blank. \n";
            isValid = false;
        }

        // Display errors (if any)
        if (isValid == false) {
            JOptionPane.showMessageDialog(null, errorMsg, "Errors", JOptionPane.ERROR_MESSAGE);
        }       
        return isValid;
    }
      
    /**
     * Check the data entered into the form controls for a new competition result is valid.
     *     
     * @param compGame      Name of the game played.
     * @param compLocation  Location game was played.
     * @param compDate      Date game was played.
     * @param compTeam      The team playing.
     * @param compPoints    How many points were won.
     * @return true if all form fields are valid, false if any field is left blank or points not a number.
     */
    public Boolean validateNewCompetition(String compGame, String compLocation, String compDate, String compTeam, String compPoints) {

        Boolean isValid = true;
        String errorMsg = "Error(s) were detected: \n";
        
        // validate Team
        if (compTeam.isEmpty()) {
            errorMsg += "Team Name is not valid. Cannot be blank. \n";
            isValid = false;
        }
        
        // validate Contact Person
        if (compGame.isEmpty()) {
            errorMsg += "Game is not valid. Cannot be blank. \n";
            isValid = false;
        }

        // validate Contact Phone
        if (compLocation.isEmpty()) {
            errorMsg += "Location is not valid. Cannot be blank. \n";
            isValid = false;
        }        
        
        // validate Contact Email
        if (compDate.isEmpty()) {
            errorMsg += "Date is not valid. Cannot be blank. \n";
            isValid = false;
        }

        if (compPoints.isEmpty()) {
            errorMsg += "Points is not valid. Cannot be blank. \n";
            isValid = false;
        }
        else {
            try {
                int points = Integer.parseInt(compPoints);
                if (points < 0 || points > 2) {
                   errorMsg += "Points is not valid. Must be between 0 and 2";
                   isValid = false;
                }
            }
            catch (Exception e) {
                errorMsg += "Points is not valid. Needs to be a number";
                isValid = false;
            }
        }     


        // Display errors (if any)
        if (isValid == false) {
            JOptionPane.showMessageDialog(null, errorMsg, "Errors", JOptionPane.ERROR_MESSAGE);
        }       
        return isValid;
    }
    
    /**
     * Save the current team data to external "teams.csv" file.
     * Notify on error.
     *     
     * @return
     */
    private void saveTeamData() {
        
        // try with resources
        try(BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("teams.csv"), "UTF-8"))) {
           for (int i = 0; i < teamList.size(); i++) {
               bufferedWriter.write(teamList.get(i).toString());
               bufferedWriter.newLine();
           }
        }
        catch (IOException ioe) {
            JOptionPane.showMessageDialog(null, ioe.getMessage(), "Save Team Data - File Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Save the current competition data to external "competitions.csv" file.
     * Notify on error. 
     *     
     * @return
     */
    private void saveCompetitionData() {
        // save data out to external 'competitions.csv' 

        // try with resources
        try(BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("competitions.csv"), "UTF-8"))) {
           for (int i = 0; i < competitionList.size(); i++) {
               bufferedWriter.write(competitionList.get(i).toString());
               bufferedWriter.newLine();
           }
        }
        catch (IOException ioe) {
            JOptionPane.showMessageDialog(null, ioe.getMessage(), "Save Competition Data - File Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * On closing the application, prompt user for confirmation to save changes.
     * If Yes: run saveCompetitionData() and saveTeamData() methods.
     * If No: Exit the application without saving.
     *     
     * @param evt the event that occurred
     * @return
     */
    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing

        // write to file here
        // yes or no variable for user to select save / not save
        int yesOrNo = JOptionPane.showConfirmDialog(null, "Do you wish to save changes before cosing?", "Save Changes?", JOptionPane.YES_NO_OPTION);
        
        if (yesOrNo == JOptionPane.YES_OPTION) {
            saveCompetitionData();
            saveTeamData();
        }      
        // exit
    }//GEN-LAST:event_formWindowClosing

    /**
     * When "Add Competition Result" button clicked, gather data from the form fields.
     * validate the data with validateNewCompetition() and return a boolean;
     * If true: Show confirmation dialog box to save. If false: Show error dialog box with error(s) listed.
     * If save: Add data to in competitionList (type:ArrayList), run displayCompetitions method.
     * 
     * @param evt the event that occurred
     * @return
     */
    private void addresult_JButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addresult_JButtonActionPerformed
        // TODO add your handling code here:
        String compGame = addGame_JTextField.getText();
        String compLocation = addLocation_JTextField.getText();
        String compDate = addDate_JTextField.getText();
        String compTeam = selectTeam_JComboBox.getSelectedItem().toString();
        String compPoints = addPoints_JTextField.getText();
        
        Boolean valid = validateNewCompetition(compGame, compLocation, compDate, compTeam, compPoints);
        if (valid == true) {
            int yesOrNo = JOptionPane.showConfirmDialog(null, "You are about to enter a new team result for: " + compTeam, "Save Changes?", JOptionPane.YES_NO_OPTION);
 
            if (yesOrNo == JOptionPane.YES_OPTION) {
                Competition competition = new Competition(compGame, compLocation, compDate, compTeam, Integer.parseInt(compPoints));
                competitionList.add(competition);    
                displayCompetitions(); 
            }       
        }      
    }//GEN-LAST:event_addresult_JButtonActionPerformed

    /**
     * Display dialog box containing the top 5 teams.
     * msg variable contains teams. Passed to messageDialog.
     * 
     * @param evt the event that occurred
     * @return
     */
    private void topTeams_JButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_topTeams_JButtonActionPerformed
        // TODO add your handling code here:
        String msg = displayTopTeams(leaderboard);
        JOptionPane.showMessageDialog(null, msg, "Top Teams Leaderboard", JOptionPane.INFORMATION_MESSAGE);   
    }//GEN-LAST:event_topTeams_JButtonActionPerformed

    /**
     * Update existing team information.
     * use selectedIndex of dropdown item to reference the correct team in the teamList.
     * 
     * @param evt the event that occurred
     * @return
     */
    private void updateTeam_JButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_updateTeam_JButtonActionPerformed
        // TODO add your handling code here:
        
        // get a reference to the selected teams index
        int updateIndex = updateTeam_JComboBox.getSelectedIndex();
        
        // get the filled in form fields
        String updateTeam = updateTeam_JComboBox.getSelectedItem().toString();
        String updatePerson = uContactPerson_JTextField.getText();
        String updatePhone = uContactPh_JTextField.getText();
        String updateEmail = uContactEm_JTextField.getText();
       
        // validate
        Boolean valid = validateNewTeam(updateTeam, updatePerson, updatePhone, updateEmail);
        if (valid) {
           
            // showDialog
            int yesOrNo = JOptionPane.showConfirmDialog(null, "You are about to update a new team: " + updateTeam, "Save Changes?", JOptionPane.YES_NO_OPTION);
            
            if (yesOrNo == JOptionPane.YES_OPTION) { 
                
                // update with new form field values using the previously grabbed index
                // Do this here so the fields are only updated if the user confirms they want to save.
                teamList.get(updateIndex).setContactPerson(updatePerson);
                teamList.get(updateIndex).setContactPhone(updatePhone);
                teamList.get(updateIndex).setContactEmail(updateEmail);               
            }       
        }
        // refresh the existing teams. If there are errors on validation having this here means
        // the fields will be repopulated with the original values rather than just being left blank
        
        displayTeamDetails();       
    }//GEN-LAST:event_updateTeam_JButtonActionPerformed

    
    /**
    * Takes a list of class objects and outputs the top 5 teams in a multi line string
    * to be used inside the Teams Leaderboard dialog modal window.
    *
    * @param leaderboard    contains a list of Team objects.
    * @return string concatenated with the top 5 teams.
    */
    private String displayTopTeams(List<Team> leaderboard) {
        
        String topTeams = "Points  |  Team\n";  
        for (int count = 0; count < leaderboard.size(); count++) {
            if (count < 5) {
                Team team = leaderboard.get(count);
                // System.out.println(t.getTeamName() + "\t\t" + t.getTotalPoints());
                topTeams += team.getTotalPoints() + "    " + team.getTeamName() + "\n";               
            }
            else {
                break;
            }
        }
        return topTeams;
    }
    
    // Hans leaderboard for reference / sorting
    
    private void altLeaderBoard() {
        if (competitionList.size() > 0 && teamList.size() > 0) {
            String leaderBoardStr = "TEAMS LEADER BOARD\n\nPoints   Team\n";
            ArrayList<String>teams_ArrayList = new ArrayList<String>();
            ArrayList<Integer>points_ArrayList = new ArrayList<Integer>();
            
            for (int i = 0; i < teamList.size(); i++) {
                //get team name
                String teamName = teamList.get(i).getTeamName();
                // declare totalPoints and set to zero
                int totalPoints = 0;
                //loop through the competitionList to total points for each team
                for (int j = 0; j < competitionList.size(); j++) {
                    if (teamName.equals(competitionList.get(j).getCompetitionTeam())) {
                        totalPoints += competitionList.get(j).getCompetitionPoints();
                    }
                }
                // add teamName string to teams_ArrayList
                teams_ArrayList.add(teamName);
                // add totalPoints integer to points_ArrayList
                points_ArrayList.add(totalPoints);
                // System.out.println(teamList.get(i).getTeamName() + "Total Points " + totalPoints);
                // leaderBoardStr += totalPoints + " " + teamList.get(i).getCompetitionTeam() + "\n";
            }
            // store total points
            // sort teams and points in point descending order
            // order the lists by the total points accumulate (descending order
            // using a selection sort algorithm ( with 2 for loops )
            for (int i = 0; i < points_ArrayList.size() - 1; i++) {
                for (int j = i + 1; j < points_ArrayList.size(); j++) {
                    if(points_ArrayList.get(j) > points_ArrayList.get(i)) {
                        // swap integers (total points)
                        int tempInt = points_ArrayList.get(j);
                        points_ArrayList.set(j, points_ArrayList.get(i));
                        points_ArrayList.set(i, tempInt);
                        // swap strings (team names)
                        String tempStr = teams_ArrayList.get(j);
                        teams_ArrayList.set(j, teams_ArrayList.get(i));
                        teams_ArrayList.set(i, tempStr);
                    }
                }
            }
            for (int i = 0; i < points_ArrayList.size(); i++) {
                leaderBoardStr += points_ArrayList.get(i) + "    " + teams_ArrayList.get(i) + "\n";
            }
            // display each team with points in JOptionPane window
            JOptionPane.showMessageDialog(null, leaderBoardStr, "TEAMS LEADER BOARD", JOptionPane.INFORMATION_MESSAGE);         
        }
    }
    
    // end Hans sorting
    
   
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> new GCESports_GUI().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel addDate_JLabel;
    private javax.swing.JTextField addDate_JTextField;
    private javax.swing.JLabel addGame_JLabel;
    private javax.swing.JTextField addGame_JTextField;
    private javax.swing.JLabel addHeading_JLabel;
    private javax.swing.JLabel addLocation_JLabel;
    private javax.swing.JTextField addLocation_JTextField;
    private javax.swing.JLabel addNewResult_JLabel;
    private javax.swing.JButton addNewTeam_JButton;
    private javax.swing.JLabel addPoints_JLabel;
    private javax.swing.JTextField addPoints_JTextField;
    private javax.swing.JPanel addResult_JPanel;
    private javax.swing.JLabel addTeam_JLabel;
    private javax.swing.JPanel addTeam_JPanel;
    private javax.swing.JButton addresult_JButton;
    private javax.swing.JPanel body_JPanel;
    private javax.swing.JTable compResults_JTable;
    private javax.swing.JLabel contactEm_JLabel;
    private javax.swing.JTextField contactEm_JTextField;
    private javax.swing.JLabel contactPerson_JLabel;
    private javax.swing.JTextField contactPerson_JTextField;
    private javax.swing.JLabel contactPh_JLabel;
    private javax.swing.JTextField contactPh_JTextField;
    private javax.swing.JTabbedPane content_JTabbedPanel;
    private javax.swing.JPanel header_JPanel;
    private javax.swing.JLabel image_JLabel;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel resultHeading_JLabel;
    private javax.swing.JPanel result_JPanel;
    private javax.swing.JComboBox<String> selectTeam_JComboBox;
    private javax.swing.JLabel teamName_JLabel;
    private javax.swing.JTextField teamName_JTextField;
    private javax.swing.JLabel team_JLabel;
    private javax.swing.JButton topTeams_JButton;
    private javax.swing.JLabel uContactEm_JLabel;
    private javax.swing.JTextField uContactEm_JTextField;
    private javax.swing.JLabel uContactPerson_JLabel;
    private javax.swing.JTextField uContactPerson_JTextField;
    private javax.swing.JLabel uContactPh_JLabel;
    private javax.swing.JTextField uContactPh_JTextField;
    private javax.swing.JLabel uTeamHeading_JLabel;
    private javax.swing.JButton updateTeam_JButton;
    private javax.swing.JComboBox<String> updateTeam_JComboBox;
    private javax.swing.JPanel updateTeam_JPanel;
    // End of variables declaration//GEN-END:variables
}
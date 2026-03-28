package gcesports_gui;

public class Competition {
    
   private String competitionDate;
   private String competitionLocation;
   private String competitionGame;
   private String competitionTeam;
   private int competitionPoints;
   
   public Competition(String competitionGame, String competitionLocation, String competitionDate, String competitionTeam, int competitionPoints) {
       this.competitionGame = competitionGame;
       this.competitionLocation = competitionLocation;
       this.competitionDate = competitionDate;
       this.competitionTeam = competitionTeam;
       this.competitionPoints = competitionPoints;
   }
   
   // ACCESSORS
   public String getCompetitionDate() {
       return this.competitionDate;
   }
   
   public String getCompetitionLocation() {
       return this.competitionLocation;
   }
   
   public String getCompetitionGame() {
       return this.competitionGame;
   }
   
   public String getCompetitionTeam() {
       return this.competitionTeam;
   }
   
   public int getCompetitionPoints() {
       return this.competitionPoints;
   }
   
   
   // MUTATORS
   public void setCompetitionDate(String competitionDate) {
       this.competitionDate = competitionDate;
   }
   
   public void setCompetitionLocation(String competitionLocation) {
       this.competitionLocation = competitionLocation;
   }
   
   public void setCompetitionGame(String competitionGame) {
       this.competitionGame = competitionGame;
   }
   
   public void setCompetitionTeam(String competitionTeam) {
       this.competitionTeam = competitionTeam;
   }
   
   public void setCompetitionPoints(int competitionPoints) {
       this.competitionPoints = competitionPoints;
   }
   
   // OVERRIDE - THIS WRITES OUT FOR THE EXPORT FUNCTION
   @Override
   public String toString() {
       String compInfo = competitionDate + ",";
              compInfo += competitionLocation + ",";
              compInfo += competitionGame + ",";
              compInfo += competitionTeam + ",";
              compInfo += competitionPoints;
              
        return compInfo;
   }  
}

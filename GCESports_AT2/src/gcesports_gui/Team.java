package gcesports_gui;

public class Team implements Comparable<Team> {
    
    private String teamName;
    private String contactPerson;
    private String contactPhone;
    private String contactEmail;
    private int totalPoints;
    
    public Team(String teamName, int totalPoints) {
        this.teamName = teamName;
        this.totalPoints = totalPoints;
    }
    
    public Team(String teamName, String contactPerson, String contactPhone, String contactEmail) {
        this.teamName = teamName;
        this.contactPerson = contactPerson;
        this.contactPhone = contactPhone;
        this.contactEmail = contactEmail;
    }
    
    // ACCESSORS
    public String getTeamName() {
        return teamName;
    }
    
    public String getContactPerson() {
        return contactPerson;
    }
    
    public String getContactPhone() {
        return contactPhone;
    }
    
    public String getContactEmail() {
        return contactEmail;
    }
    
    public int getTotalPoints() { 
        return totalPoints; 
    }

    // MUTATORS
    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }
    
    public void setContactPerson(String contactPerson) {
        this.contactPerson = contactPerson;
    }
    
    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }
    
    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }
  
    
    // OVERRIDE
   @Override
   public String toString() {
       String teamInfo = teamName + ",";
              teamInfo += contactPerson + ",";
              teamInfo += contactPhone + ",";
              teamInfo += contactEmail;
              
        return teamInfo;
   }
   
   @Override
    public int compareTo(Team other) {
        // Compare the other team's score to this one for Descending order
        return Integer.compare(other.totalPoints, this.totalPoints);
    }
}
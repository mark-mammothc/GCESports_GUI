package gcesports_gui;

public class Team {
    
    private String teamName;
    private String contactPerson;
    private String contactPhone;
    private String contactEmail;
    
    // For Technical Report
    public Team() {
        this.teamName = "Generic_Name";
        this.contactPerson = "Generic_Person";
        this.contactPhone = "Generic_Phone";
        this.contactEmail = "Generic_Email";   
    }
    
    public Team(String teamName, String contactPerson, String contactPhone, String contactEmail) {
        this.teamName = teamName;
        this.contactPerson = contactPerson;
        this.contactPhone = contactPhone;
        this.contactEmail = contactEmail;
    }
    
    // ACCESSORS
    public String getTeamName() {
        return this.teamName;
    }
    
    public String getContactPerson() {
        return this.contactPerson;
    }
    
    public String getContactPhone() {
        return this.contactPhone;
    }
    
    public String getContactEmail() {
        return this.contactEmail;
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
    
}

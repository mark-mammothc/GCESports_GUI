/*
   Filename:    Team.java
   Purpose:     Provide team information for CGESports GUI AT2 
   Author:      Mark Walsh
   Date:        27-Mar-2026
   Version:     2.0
   License (if applicable): N/A
   Notes, Fixes, Updates: Added 2nd constructor, totalPoints instance variable and an additional override for dealing with high scores
*/

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
   
   /*   After trying to use a Set and a couple of other methods i went back to the idea of a Map.
        In combination with google searches and some back n forth with Gemini to learn the "WHY" 
        of how this technique works, i opted to use a Map to hold data from a class obj, making it 
        easy to hold / call both team + score. The list usage is to take advantage of being able to easily 
        sort / compare a List in order.
   
        I know there are multiple other ways to achieve this. I just wanted to learn a little bit
        about this technique and also practise problem solving.
    */
   
   
   @Override
    public int compareTo(Team other) {
        // Compare the other team's score to this one
        return Integer.compare(other.totalPoints, this.totalPoints);
    }
}
package com.joshuacurcio.beverageservice;

/**
 * Created by sammy on 10/29/2016.
 */

public class UserProfile
{
    private String FirstName;
    private String LastName;
    private String Email;
    private String Address;

    public UserProfile(String FirstName, String LastName, String Email, String Address)
    {
        this.FirstName = FirstName;
        this.LastName = LastName;
        this.Email = Email;
        this.Address = Address;
    }

    public String getFirstName()
    {
        return FirstName;
    }

    public void setFirstName(String FirstName)
    {
        this.FirstName = FirstName;
    }

    public String getLastName()
    {
        return LastName;
    }

    public void setLastName(String LastName)
    {
        this.LastName = LastName;
    }

    public String getEmail()
    {
        return Email;
    }

    public void setEmail(String Email)
    {
        this.Email = Email;
    }

    public String getAddress()
    {
        return Address;
    }

    public void setAddress(String Address)
    {
        this.Address = Address;
    }

}

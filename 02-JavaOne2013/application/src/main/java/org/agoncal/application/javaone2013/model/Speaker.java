package org.agoncal.application.javaone2013.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * @author Antonio Goncalves
 *         http://www.antoniogoncalves.org
 *         --
 */
@Entity
@NamedQueries({
        @NamedQuery(name = "findAllSpeakers", query = "SELECT s FROM Speaker s")
})
@XmlRootElement
public class Speaker implements Serializable {

    // ======================================
    // =             Attributes             =
    // ======================================

    @Id
    @GeneratedValue
    private Long id;
    @Version
    private int version;
    @NotNull
    private String firstname;
    @NotNull
    private String surname;
    private String bio;
    private String twitter;

    // ======================================
    // =            Constructors            =
    // ======================================

    public Speaker() {
    }

    public Speaker(String firstname, String surname, String bio, String twitter) {
        this.firstname = firstname;
        this.surname = surname;
        this.bio = bio;
        this.twitter = twitter;
    }

    // ======================================
    // =          Getters & Setters         =
    // ======================================

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getTwitter() {
        return twitter;
    }

    public void setTwitter(String twitter) {
        this.twitter = twitter;
    }
}
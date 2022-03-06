package entity;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "diagnoses")
public class Diagnose extends BaseEntity{

    private String name;
    private String comments;
    private GP gp;

    public Diagnose(){
    }

    @Column(name = "name", nullable = false)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "comments", columnDefinition = "TEXT")
    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    @ManyToOne
    public GP getGp() {
        return gp;
    }

    public void setGp(GP gp) {
        this.gp = gp;
    }
}

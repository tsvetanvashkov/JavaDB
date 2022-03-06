package entity;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "gp")
public class GP extends BaseEntity{

    private Set<Patients> patients;
    private Set<Visitation> visitations;
    private Set<Diagnose> diagnoses;
    private Set<Medicament> medicaments;

    public GP() {
        this.patients = new HashSet<>();
        this.visitations = new HashSet<>();
        this.diagnoses = new HashSet<>();
        this.medicaments = new HashSet<>();
    }

    @OneToMany(mappedBy = "gp")
    public Set<Patients> getPatients() {
        return patients;
    }

    public void setPatients(Set<Patients> patients) {
        this.patients = patients;
    }

    @OneToMany(mappedBy = "gp")
    public Set<Visitation> getVisitations() {
        return visitations;
    }

    public void setVisitations(Set<Visitation> visitations) {
        this.visitations = visitations;
    }

    @OneToMany(mappedBy = "gp")
    public Set<Diagnose> getDiagnoses() {
        return diagnoses;
    }

    public void setDiagnoses(Set<Diagnose> diagnoses) {
        this.diagnoses = diagnoses;
    }

    @OneToMany(mappedBy = "gp")
    public Set<Medicament> getMedicaments() {
        return medicaments;
    }

    public void setMedicaments(Set<Medicament> medicaments) {
        this.medicaments = medicaments;
    }
}

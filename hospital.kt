class Doctor(docId: String, docName: String, docSpecialty: String) {
    var id: String = docId
    var name: String = docName
    var specialty: String = docSpecialty
    var activePatients: Int = 0
}

class Patient(patId: String, patName: String, patIllness: String) {
    var id: String = patId
    var name: String = patName
    var Illness: String = patIllness
    var isAdmitted: Boolean = false
}

class AdmissionRecord(pId: String, dId: String, day: Int) {
    var patientId: String = pId
    var doctorId: String = dId
    var dayAdmitted: Int = day
    var isDischarged: Boolean = false
}

class HospitalManagement {
    var doctors = arrayOfNulls<Doctor>(10)
    var patients = arrayOfNulls<Patient>(10)
    var admissions = arrayOfNulls<AdmissionRecord>(20)
    var currentDay: Int = 1

    fun advanceDay() {
        currentDay++
        println("\n>>> A day has passed. It is now Day " + currentDay + " <<<")
    }

    fun addDoctor(id: String, name: String, specialty: String) {
        for (i in 0 until 10) {
            if (doctors[i] == null) {
                doctors[i] = Doctor(id, name, specialty)
                println("Hospital: Added Dr. " + name + " (" + specialty + ")")
                return
            }
        }
        println("Hospital: System is full! Cannot add more doctors.")
    }

    fun addPatient(id: String, name: String, Illness: String) {
        for (i in 0 until 10) {
            if (patients[i] == null) {
                patients[i] = Patient(id, name, Illness)
                println("Hospital: Registered patient '" + name + "' at reception.")
                return
            }
        }
        println("Hospital: Patient registry is full.")
    }

    fun displayDoctors() {
        println("\n--- Available Doctors ---")
        var count = 0
        for (i in 0 until 10) {
            var d = doctors[i]
            if (d != null) {
                println("ID: " + d.id + " | Dr. " + d.name + " | Specialty: " + d.specialty + " | Active Patients: " + d.activePatients)
                count++
            }
        }
        if (count == 0) println("No doctors in the system.")
    }

    fun displayPatients() {
        println("\n--- Registered Patients ---")
        var count = 0
        for (i in 0 until 10) {
            var p = patients[i]
            if (p != null) {
                var status = if (p.isAdmitted) "Admitted" else "Not Admitted"
                println("ID: " + p.id + " | Name: " + p.name + " | Illness: " + p.Illness + " | Status: " + status)
                count++
            }
        }
        if (count == 0) println("No patients registered.")
    }

    fun deleteDoctor(doctorId: String) {
        for (i in 0 until 10) {
            var d = doctors[i]
            if (d != null && d.id == doctorId) {
                if (d.activePatients > 0) {
                    println("Error: Cannot delete doctor! They are currently treating " + d.activePatients + " patient(s).")
                    return
                }
                doctors[i] = null
                println("Hospital: Doctor deleted.")
                return
            }
        }
        println("Hospital: Doctor ID not found.")
    }

    fun deletePatient(patientId: String) {
        for (i in 0 until 10) {
            var p = patients[i]
            if (p != null && p.id == patientId) {
                if (p.isAdmitted) {
                    println("Error: Cannot delete patient! They are currently admitted and must be discharged first.")
                    return
                }
                patients[i] = null
                println("Hospital: Patient deleted from registry.")
                return
            }
        }
        println("Hospital: Patient ID not found.")
    }

    fun admitPatient(patientId: String, doctorId: String) {
        var foundPatient: Patient? = null
        var foundDoctor: Doctor? = null

        for (i in 0 until 10) {
            if (patients[i] != null && patients[i]?.id == patientId) foundPatient = patients[i]
            if (doctors[i] != null && doctors[i]?.id == doctorId) foundDoctor = doctors[i]
        }

        if (foundPatient != null && foundDoctor != null) {
            if (!foundPatient.isAdmitted) {
                for (i in 0 until 20) {
                    if (admissions[i] == null) {
                        admissions[i] = AdmissionRecord(patientId, doctorId, currentDay)
                        foundPatient.isAdmitted = true
                        foundDoctor.activePatients++
                        println("Hospital: " + foundPatient.name + " admitted under Dr. " + foundDoctor.name + " on Day " + currentDay + ".")
                        return
                    }
                }
                println("Hospital: Ward is completely full, cannot process more admissions.")
            } else {
                println("Hospital: Patient is already admitted.")
            }
        } else {
            println("Hospital: Invalid Patient ID or Doctor ID.")
        }
    }

    fun dischargePatient(patientId: String, doctorId: String) {
        var foundRecord: AdmissionRecord? = null
        
        for (i in 0 until 20) {
            var a = admissions[i]
            if (a != null && a.patientId == patientId && a.doctorId == doctorId && !a.isDischarged) {
                foundRecord = a
                a.isDischarged = true
                break
            }
        }

        if (foundRecord != null) {
            var daysStayed = currentDay - foundRecord.dayAdmitted
            if (daysStayed == 0) daysStayed = 1
            
            var totalBill = daysStayed * 1000
            println("=== DISCHARGE SUMMARY ===")
            println("Days Admitted: " + daysStayed)
            println("Total Hospital Bill: Rs. " + totalBill)
            println("=========================")

            for (i in 0 until 10) {
                if (patients[i] != null && patients[i]?.id == patientId) patients[i]?.isAdmitted = false
                if (doctors[i] != null && doctors[i]?.id == doctorId) doctors[i]?.activePatients = (doctors[i]?.activePatients ?: 1) - 1
            }
            println("Hospital: Patient successfully discharged on Day " + currentDay + ".")
        } else {
            println("Hospital: No active admission found for this Patient ID and Doctor ID.")
        }
    }
}

fun main() {
    var hospital = HospitalManagement()
    var running = true

    println("=== Advanced Hospital Management System ===")

    while (running) {
        println("\n--- Menu (Today is Day " + hospital.currentDay + ") ---")
        println("1. Display Doctors")
        println("2. Display Patients")
        println("3. Register Doctor")
        println("4. Register Patient")
        println("5. Delete Doctor")
        println("6. Delete Patient")
        println("7. Admit Patient")
        println("8. Discharge Patient")
        println("9. Advance to Next Day")
        println("10. Exit")
        print("Enter choice: ")
        
        var choice = readln()

        if (choice == "1") hospital.displayDoctors()
        else if (choice == "2") hospital.displayPatients()
        
        else if (choice == "3") {
            print("Enter new Doctor ID (e.g., D1): ")
            var id = readln()
            print("Enter Name: ")
            var name = readln()
            print("Enter Specialty: ")
            var specialty = readln()
            hospital.addDoctor(id, name, specialty)
        }
        
        else if (choice == "4") {
            print("Enter new Patient ID (e.g., P1): ")
            var id = readln()
            print("Enter Name: ")
            var name = readln()
            print("Enter Illness: ")
            var Illness = readln()
            hospital.addPatient(id, name, Illness)
        }
        
        else if (choice == "5") {
            hospital.displayDoctors()
            print("Enter ID of Doctor to Delete: ")
            var id = readln()
            hospital.deleteDoctor(id)
        }
        
        else if (choice == "6") {
            hospital.displayPatients()
            print("Enter ID of Patient to Delete: ")
            var id = readln()
            hospital.deletePatient(id)
        }
        
        else if (choice == "7") {
            hospital.displayPatients()
            hospital.displayDoctors()
            print("Enter Patient ID to Admit: ")
            var patientId = readln()
            print("Enter Doctor ID to Assign: ")
            var doctorId = readln()
            hospital.admitPatient(patientId, doctorId)
        }
        
        else if (choice == "8") {
            hospital.displayPatients()
            hospital.displayDoctors()
            print("Enter Patient ID to Discharge: ")
            var patientId = readln()
            print("Enter Assigned Doctor ID: ")
            var doctorId = readln()
            hospital.dischargePatient(patientId, doctorId)
        }
        
        else if (choice == "9") {
            hospital.advanceDay()
        }
        
        else if (choice == "10") {
            running = false
            println("Goodbye!")
        } else {
            println("Invalid choice.")
        }
    }
}
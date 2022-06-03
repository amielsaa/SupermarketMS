package Employee.BusinessLayer;

import Utilities.Exceptions.IllegalObjectException;
import Utilities.Exceptions.ObjectAlreadyExistsException;
import Utilities.Exceptions.ObjectNotFoundException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class WorkingConditions
{
        private String description;
        private List<TimeInterval> workingHours;
        private List<String> qualifications;

    /**
     * New constructor for DAL loading
      * @param description
     * @param workingHours
     * @param qualifications
     */
    public WorkingConditions(String description, List<TimeInterval> workingHours, List<String> qualifications) {
        this.description = description;
        this.workingHours = workingHours;
        this.qualifications = qualifications;
    }

    public WorkingConditions(String description)
        {
            this.description = description;
            this.workingHours = new ArrayList<>();
            this.qualifications = new ArrayList<>();
        }

    @Override
    public String toString() {
        return "WorkingConditions{" +
                "description='" + description + '\'' +
                ", workingHours=" + workingHours +
                ", qualifications=" + qualifications +
                '}';
    }

    public String getDescription()
        {
            return description;
        }

        public List<TimeInterval> getWorkingHours()
        {
            return Collections.unmodifiableList(workingHours);
        }

        public List<String> getQualifications()
        {
            return Collections.unmodifiableList(qualifications);
        }

        protected TimeInterval addWorkingHour(LocalDateTime start, LocalDateTime end) throws Exception
        {
            TimeInterval newWh;
            newWh = new TimeInterval(start, end);
            for(TimeInterval wh : workingHours) {
                if(wh.isOverlapping(newWh)) {
                    throw new IllegalObjectException("Cannot create time overlapping with: " + wh.toString() + ". ");
                }
            }
            this.workingHours.add(newWh);

            return newWh;
        }

        protected TimeInterval removeWorkingHour(LocalDateTime start) throws Exception
        {
            // remove all with the same starting time.
            // using iterator for safe removal.
            TimeInterval removedWh = null;
            Iterator<TimeInterval> i = workingHours.iterator();
            while(i.hasNext()) {
                TimeInterval wh = i.next();
                if(wh.getStart().isEqual(start)) {
                    removedWh = wh;
                    i.remove();
                }
            }
            if(removedWh == null)
            {
                throw new ObjectNotFoundException("No working hour with this starting time found. ");
            }
            return removedWh;
        }

        protected TimeInterval getWorkingHour(LocalDateTime start) throws Exception
        {
            for(TimeInterval wh : workingHours) {
                if(wh.getStart().isEqual(start)) {
                    return wh;
                }
            }
            throw new ObjectNotFoundException("No working hour with this starting time found. ");
        }

        protected boolean addQualification(String qualification) throws Exception{
            // add by pointer
            if(qualifications.contains(qualification)) {
                throw new ObjectAlreadyExistsException("This qualification is already in this list. ");
            }
            else {
                this.qualifications.add(qualification);
                return true;
            }
        }


        /*
        protected Qualification removeQualification(String name) throws Exception
        {
            // remove all with the same name
            // using iterator for safe removal.
            Qualification removedQ = null;
            Iterator<Qualification> i = qualifications.iterator();
            while(i.hasNext()) {
                Qualification q = i.next();
                if(q.getName().equals(name)) {
                    removedQ = q;
                    i.remove();
                }
            }
            if(removedQ == null)
            {
                throw new ObjectNotFoundException("No qualification with this name found. ");
            }
            return removedQ;
        }

        protected String getQualification(String name) throws Exception {
            for(Qualification q : qualifications) {
                if(q.getName().equals(name)) {
                    return q;
                }
            }
            throw new ObjectNotFoundException("No qualification with this name found. ");
        }

         */
        protected boolean removeQualification(String name) throws Exception{
            if(!qualifications.contains(name)){
                throw new ObjectNotFoundException("No qualification with this name found. ");
            }
            qualifications.remove(name);
            return true;
        }

        protected boolean hasQualification(String name){
            return qualifications.contains(name);
        }


}

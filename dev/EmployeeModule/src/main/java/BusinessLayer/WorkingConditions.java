package BusinessLayer;

import Utilities.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class WorkingConditions
{
        private String description;
        private List<TimeInterval> workingHours;
        private List<Qualification> qualifications;

        protected WorkingConditions(String description)
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

        public List<Qualification> getQualifications()
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

        protected Qualification addQualification(Qualification qualification) throws Exception{
            // add by pointer
            if(qualifications.contains(qualification)) {
                throw new ObjectAlreadyExistsException("This qualification is already in this list. ");
            }
            else {
                this.qualifications.add(qualification);
                return qualification;
            }
        }

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

        protected Qualification getQualification(String name) throws Exception {
            for(Qualification q : qualifications) {
                if(q.getName().equals(name)) {
                    return q;
                }
            }
            throw new ObjectNotFoundException("No qualification with this name found. ");
        }
}

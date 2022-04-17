package BusinessLayer.Objects;

import Utilities.Response;

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

        protected Response<TimeInterval> addWorkingHour(LocalDateTime start, LocalDateTime end)
        {
            TimeInterval newWh = new TimeInterval(start, end);

            for(TimeInterval wh : workingHours) {
                if(wh.isOverlapping(newWh)) {
                    return Response.makeFailure("Cannot create time overlapping with: " + wh.toString() + ". ");
                }
            }
            this.workingHours.add(newWh);

            return Response.makeSuccess(newWh);
        }

        protected Response<TimeInterval> removeWorkingHour(LocalDateTime start) {
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
                return Response.makeFailure("No working hour with this starting time found. ");
            }
            return Response.makeSuccess(removedWh);
        }

        protected Response<TimeInterval> getWorkingHour(LocalDateTime start) {
            for(TimeInterval wh : workingHours) {
                if(wh.getStart().isEqual(start)) {
                    return Response.makeSuccess(wh);
                }
            }
            return Response.makeFailure("No working hour with this starting time found. ");
        }

        protected Response<Qualification> addQualification(Qualification qualification) {
            // add by pointer
            if(qualifications.contains(qualification)) {
                return Response.makeFailure("This qualification is already in this list. ");
            }
            else {
                return Response.makeSuccess(qualification);
            }
        }

        protected Response<Qualification> removeQualification(String name) {
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
                return Response.makeFailure("No qualification with this name found. ");
            }
            return Response.makeSuccess(removedQ);
        }

        protected Response<Qualification> getQualification(String name) {
            for(Qualification q : qualifications) {
                if(q.getName().equals(name)) {
                    return Response.makeSuccess(q);
                }
            }
            return Response.makeFailure("No qualification with this name found. ");
        }
}

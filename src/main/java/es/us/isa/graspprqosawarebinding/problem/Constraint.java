package es.us.isa.graspprqosawarebinding.problem;

import es.us.isa.graspprqosawarebinding.Binding;

public abstract class Constraint {

   public boolean isMet(Binding b) {
	   return meetingDistance(b)==0.0;
   }

   public abstract Double meetingDistance(Binding b);
}

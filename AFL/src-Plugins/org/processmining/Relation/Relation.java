package org.processmining.Relation;

public enum Relation {
	DirectFollow,
	DirectCasually,
	Parallel,
	NoRel,
	Loop2,
	reDirectFollow,
	reDirectCasually,
	reParallel,
	reNoRel,
	reLoop2;
	public String toString()
	{
		String symbale = new String();
		symbale = "";
		switch(this)
		{
		   case DirectFollow:
			   symbale = " >";
			   break;
		   case DirectCasually:
			   symbale = "->";
			   break;
		   case Parallel:
			   symbale = "||";
			   break;
		   case Loop2:
			   symbale = "O";
			   break;
		   case reDirectFollow:
			   symbale = " <";
			   break;
		   case reDirectCasually:
			   symbale = "<-";
			   break;
		   case reParallel:
			   symbale = "||";
			   break;
		   case reLoop2:
			   symbale = "O";
			   break;
		   default:
			   symbale = " #";
			   break;
		}
		return symbale;
	}
	public Relation getReRelation()
	{
		Relation symbale;
		switch(this)
		{
		   case DirectFollow:
			   symbale = Relation.reDirectFollow;
			   break;
		   case DirectCasually:
			   symbale = Relation.reDirectCasually;
			   break;
		   case Parallel:
			   symbale = Relation.reParallel;
			   break;
		   case Loop2:
			   symbale = Relation.reLoop2;
			   break;
		   case reDirectFollow:
			   symbale = Relation.DirectFollow;
			   break;
		   case reDirectCasually:
			   symbale = Relation.DirectCasually;
			   break;
		   case reParallel:
			   symbale = Relation.Parallel;
			   break;
		   case reLoop2:
			   symbale = Relation.Loop2;
			   break;
		   default:
			   symbale = Relation.NoRel;
			   break;
		}
		return symbale;
	}

}

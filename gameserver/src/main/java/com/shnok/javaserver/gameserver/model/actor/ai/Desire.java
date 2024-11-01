package com.shnok.javaserver.gameserver.model.actor.ai;

/**
 * A datatype used as a simple "wish" for Npc.<br>
 * <br>
 * The weight is used to order the priority of the related {@link Intention}.
 */
public class Desire extends Intention
{
	private double _weight;
	private long _timeStamp;
	
	public Desire(double weight, long timeStamp)
	{
		super();
		
		_weight = weight;
		_timeStamp = timeStamp;
	}
	
	@Override
	public boolean equals(Object obj)
	{
		return super.equals(obj);
	}
	
	@Override
	public int compareTo(Intention other)
	{
		if (other instanceof Desire otherDesire)
		{
			// Compare by weight
			double weightCompare = Double.compare(otherDesire.getWeight(), getWeight());
			if (weightCompare != 0.0)
				return weightCompare > 0 ? 1 : weightCompare < 0 ? -1 : 0;
		}
		
		return super.compareTo(other);
	}
	
	@Override
	public int hashCode()
	{
		return super.hashCode();
	}
	
	@Override
	public String toString()
	{
		return "Desire [type=" + _type.toString() + " weight=" + _weight + "]";
	}
	
	public double getWeight()
	{
		return _weight;
	}
	
	public void addWeight(double value)
	{
		_weight = Math.min(_weight + value, Double.MAX_VALUE);
	}
	
	public void reduceWeight(double value)
	{
		_weight -= value;
	}
	
	public long getTimeStamp()
	{
		return _timeStamp;
	}
}
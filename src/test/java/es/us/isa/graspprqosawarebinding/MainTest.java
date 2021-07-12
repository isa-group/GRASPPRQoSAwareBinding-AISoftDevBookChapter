package es.us.isa.graspprqosawarebinding;

import static org.junit.Assert.assertEquals;

import java.util.Map;

import org.junit.Test;

import es.us.isa.graspprqosawarebinding.problem.QoSAwareBindingProblem;

public class MainTest {
	
	public static final double delta=10e-15;
	
	@Test
	public void computeMaxAndMinsTest()
	{
		Main main=new Main();
		QoSAwareBindingProblem problem=main.buildSampleProblem();
		Map<QoSProperty,Double> max=main.getBindingMax();
		Map<QoSProperty,Double> min=main.getBindingMin();
		Double maxCost=max.get(main.getCost());
		Double minCost=min.get(main.getCost());
		Double maxAvailability=max.get(main.getAvailability());
		Double minAvailability=min.get(main.getAvailability());
		Double maxStorage=max.get(main.getStorage());
		Double minStorage=min.get(main.getStorage());
		assertEquals(new Double(0.45),minCost,delta);
		assertEquals(new Double(22.315),maxCost,delta);
		assertEquals(new Double(198.95),minAvailability,delta);
		assertEquals(new Double(199.94),maxAvailability,delta);
		assertEquals(new Double(2.0),minStorage,delta);
		assertEquals(new Double(2048.0),maxStorage,delta);
	}
}

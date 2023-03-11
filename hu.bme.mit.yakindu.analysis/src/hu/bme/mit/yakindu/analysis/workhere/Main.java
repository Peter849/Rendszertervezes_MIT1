package hu.bme.mit.yakindu.analysis.workhere;

import java.util.ArrayList;

import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EObject;
import org.junit.Test;
import org.yakindu.sct.model.sgraph.State;
import org.yakindu.sct.model.sgraph.Statechart;
import org.yakindu.sct.model.sgraph.Transition;

import hu.bme.mit.model2gml.Model2GML;
import hu.bme.mit.yakindu.analysis.modelmanager.ModelManager;

public class Main {
	@Test
	public void test() {
		main(new String[0]);
	}
	
	private static ArrayList<String> stateNames = new ArrayList<String>();
	
	public static void main(String[] args) {
		ModelManager manager = new ModelManager();
		Model2GML model2gml = new Model2GML();
		
		// Loading model
		EObject root = manager.loadModel("model_input/example.sct");
		
		// Reading model
		Statechart s = (Statechart) root;
		TreeIterator<EObject> iterator = s.eAllContents();
		while (iterator.hasNext()) {
			EObject content = iterator.next();
			if(content instanceof State) {
				State state = (State) content;
				int nameID = 0;
				int numberOfOutgoingTransitions = state.getOutgoingTransitions().size();
				
				//2.3.
				for(int i = 0; i < numberOfOutgoingTransitions; i++) {
					Transition transition = state.getOutgoingTransitions().get(i);
					System.out.println(transition.getSource().getName() + " -> " + transition.getTarget().getName());
				}
				
				//2.4.
				if(numberOfOutgoingTransitions == 0)
					System.out.println(state.getName());
				
				//2.5., 2.6.
				while(state.getName().equals(" ")) {
					String currentName = new String("name" + nameID);
					if(!stateNames.contains(currentName)) {
						state.setName(currentName);
						stateNames.add(currentName);
						nameID += 1;
					}else {
						nameID += 1;
					}
				}
			}
		}
		
		// Transforming the model into a graph representation
		String content = model2gml.transform(root);
		// and saving it
		manager.saveFile("model_output/graph.gml", content);
	}
}

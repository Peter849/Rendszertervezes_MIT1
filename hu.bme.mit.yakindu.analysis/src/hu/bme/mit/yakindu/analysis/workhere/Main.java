package hu.bme.mit.yakindu.analysis.workhere;

import java.util.ArrayList;

import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EObject;
import org.junit.Test;
import org.yakindu.sct.model.sgraph.State;
import org.yakindu.sct.model.sgraph.Statechart;
import org.yakindu.sct.model.sgraph.Transition;
import org.yakindu.sct.model.stext.stext.EventDefinition;
import org.yakindu.sct.model.stext.stext.VariableDefinition;
import org.yakindu.sct.model.stext.stext.impl.EventDefinitionImpl;
import org.yakindu.sct.model.stext.stext.impl.VariableDefinitionImpl;

import hu.bme.mit.model2gml.Model2GML;
import hu.bme.mit.yakindu.analysis.modelmanager.ModelManager;

public class Main {
	@Test
	public void test() {
		main(new String[0]);
	}
	
	private static ArrayList<String> stateNames = new ArrayList<String>();
	//private static ArrayList<String> eventNames = new ArrayList<String>();
	//private static ArrayList<String> variableNames = new ArrayList<String>();

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
					System.out.println("Csapda állapot neve: " + state.getName());
				
				//2.5., 2.6.
				while(state.getName().equals("")) {
					String currentName = new String("name" + nameID);
					if(!stateNames.contains(currentName)) {
						state.setName(currentName);
						stateNames.add(currentName);
						nameID += 1;
						System.out.println(state.getName());
					}else {
						nameID += 1;
					}
				}
			//4.3.
			}else if(content instanceof EventDefinition) {
				EventDefinition eventDefinitionImpl = (EventDefinition) content;
				System.out.println("Bemenő esemény: " + eventDefinitionImpl.getName());
				//eventNames.add(eventDefinitionImpl.getName());
			}else if(content instanceof VariableDefinition) {
				VariableDefinitionImpl variableDefinitionImpl = (VariableDefinitionImpl) content;
				System.out.println("Belső változó: " + variableDefinitionImpl.getName());
				//variableNames.add(variableDefinitionImpl.getName());
			}
		}
		
		//4.5.
		codeGenerator(s);
		
		// Transforming the model into a graph representation
		String content = model2gml.transform(root);
		// and saving it
		manager.saveFile("model_output/graph.gml", content);
	}
	
	//4.4.
	public static void printEventsVars(Statechart s) {
		TreeIterator<EObject> iterator = s.eAllContents();
		while (iterator.hasNext()) {
			EObject content = iterator.next();
			
			if(content instanceof EventDefinition) {
				EventDefinition eventDefinition = (EventDefinition) content;
				String st=eventDefinition.getName().substring(0,1).toUpperCase()+eventDefinition.getName().substring(1, eventDefinition.getName().length());
						 System.out.println("		System.out.println(\""+eventDefinition.getName().toUpperCase()+ " = \" + s.getSCInterface().get" +st+"());" );
						 }
		}
		iterator = s.eAllContents();
		while (iterator.hasNext()) {
			EObject content = iterator.next();
			
			if(content instanceof VariableDefinitionImpl) {
				VariableDefinitionImpl variableDefinitionImpl = (VariableDefinitionImpl) content;
				String st=variableDefinitionImpl.getName().substring(0,1).toUpperCase()+variableDefinitionImpl.getName().substring(1, variableDefinitionImpl.getName().length());
						 System.out.println("		System.out.println(\""+variableDefinitionImpl.getName().toUpperCase()+ " = \" + s.getSCInterface().get" +st+"());" );
						 }
		}
		System.out.println("	}");
	}
	
	//4.5.
	public static void codeGenerator(Statechart s) {
		TreeIterator<EObject> iterator = s.eAllContents();
		System.out.println("package hu.bme.mit.yakindu.analysis.workhere;\n" + 
				"\n" + 
				"import java.io.IOException;\n" + 
				"import java.util.Scanner;\n" + 
				"\n" + 
				"import hu.bme.mit.yakindu.analysis.RuntimeService;\n" + 
				"import hu.bme.mit.yakindu.analysis.TimerService;\n" + 
				"import hu.bme.mit.yakindu.analysis.example.ExampleStatemachine;\n" + 
				"import hu.bme.mit.yakindu.analysis.example.IExampleStatemachine;\n" + 
				"\n" + 
				"\n" + 
				"\n" + 
				"public class RunStatechart {\n" + 
				"	\n" + 
				"	public static void main(String[] args) throws IOException {\n" + 
				"		ExampleStatemachine s = new ExampleStatemachine();\n" + 
				"		s.setTimer(new TimerService());\n" + 
				"		RuntimeService.getInstance().registerStatemachine(s, 200);\n\n" + 
				"		s.init();\n" + 
				"		s.enter();\n\n" + 
				"		s.runCycle();\n" + 
				"		print(s);\n\n" +
				"		Scanner input=new Scanner(System.in);\n" + 
				"		String line = input.nextLine();\n" +  
				"		while(!line.equals(\"exit\"))\n" + 
				"			System.out.print(\"Event: \");\n" + 
				"			st=sc.nextLine();\n" + 
				"			switch (st.toLowerCase()){");
		
		while (iterator.hasNext()) {
			EObject content = iterator.next();
			if(content instanceof EventDefinitionImpl) {
				EventDefinitionImpl event = (EventDefinitionImpl) content;
				String st=event.getName().substring(0,1).toUpperCase()+event.getName().substring(1, event.getName().length());
				System.out.println(
						"			case "+"\"" + event.getName() + "\"" + ":\n" + 
						"				s.raise"  +  st  +  "();\n" + 
						"				break;"
									);
			}
			
			
		}
		System.out.println(
				"			case \"exit\":\n" + 
				"				exit=true;\n" + 
				"				sc.close();\n" + 
				"				break;\n" + 
				"			default:\n" + 
				"				System.out.println(\"W\");\n" + 
				"				break;\n" + 
				"			}\n" + 
				"			s.runCycle();\n" + 
				"			print(s);\n" + 
				"		}\n" + 
				"		\n" + 
				"		s.exit();\n" +
				"		input.close();" +
				"		System.exit(0);\n" + 
				"	}");
		
		System.out.println("	public static void print(IExampleStatemachine s) {");
		iterator = s.eAllContents();
		while (iterator.hasNext()) {
			EObject content = iterator.next();
			
			if(content instanceof VariableDefinitionImpl) {
				VariableDefinitionImpl variableDefinitionImpl = (VariableDefinitionImpl) content;
				String st=variableDefinitionImpl.getName().substring(0,1).toUpperCase()+variableDefinitionImpl.getName().substring(1, variableDefinitionImpl.getName().length());
						 System.out.println("		System.out.println(\""+variableDefinitionImpl.getName().toUpperCase()+ " = \" + s.getSCInterface().get" +st+"());" );
						 }
		}
		System.out.println("	}");
		System.out.println("}");
	}
}

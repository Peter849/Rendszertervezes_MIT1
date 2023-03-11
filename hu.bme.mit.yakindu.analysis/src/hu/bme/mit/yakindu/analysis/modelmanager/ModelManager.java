package hu.bme.mit.yakindu.analysis.modelmanager;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;

import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.eclipse.gmf.runtime.notation.NotationPackage;
import org.yakindu.base.expressions.ExpressionsStandaloneSetup;
import org.yakindu.sct.model.sgraph.SGraphPackage;
import org.yakindu.sct.model.stext.STextStandaloneSetup;
import org.yakindu.sct.model.stext.resource.StextResourceFactory;
import org.yakindu.sct.model.stext.stext.StextPackage;

import com.google.inject.Injector;

public class ModelManager {
	ResourceSet resourceSet;
	Injector injector;
	
	public ModelManager() {
		init();
	}
	
	public void init() {
		SGraphPackage.eINSTANCE.eClass();
		StextPackage.eINSTANCE.eClass();
		
		NotationPackage.eINSTANCE.eClass();
		ExpressionsStandaloneSetup.doSetup();
		STextStandaloneSetup.doSetup();
		
		resourceSet = new ResourceSetImpl();
		if(Platform.isRunning()) {
			Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("sct", new StextResourceFactory());
		} else {
			Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("sct", new XMIResourceFactoryImpl());
		}
	}
	
	public EObject loadModel(String path) {
		Resource resource = this.resourceSet.getResource(URI.createURI(path), true);
		return resource.getContents().get(0);
	}
	
	public boolean saveModel(EObject root, String path) {
		Resource resource = this.resourceSet.createResource(URI.createURI(path));
		resource.getContents().add(root);
		try {
			resource.save(null);
			return true;
		} catch (IOException e) {
			System.err.println("Unable to save file: "+path);
			return false;
		}
	}
	
	public boolean saveFile(String path, String content) {
		try (Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(path), "utf-8"))) {
		   writer.write(content.toString());
		   return true;
		} catch (IOException ex) {
			System.err.println("Unable to save file: "+path);
			return false;
		}
	}
}

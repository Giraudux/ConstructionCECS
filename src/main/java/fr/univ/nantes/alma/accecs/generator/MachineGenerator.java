package fr.univ.nantes.alma.accecs.generator;

import fr.univ.nantes.alma.accecs.model.Machine;
import fr.univ.nantes.alma.accecs.model.Property;
import fr.univ.nantes.alma.accecs.model.Variable;

import org.jtwig.JtwigModel;
import org.jtwig.JtwigTemplate;

import java.io.File;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
//import java.util.logging.Logger;

/**
 *Class MachineGenerator which generate a file.mch from the Java Class Machine
 */
public class MachineGenerator implements IMachineGenerator {
    //private static final Logger LOGGER = Logger.getLogger(MachineGenerator.class.getName());
	
	/**
	 * @param machine : Machine as a Java Class
	 * @param templeFile : The template file 
	 * @param outputStream : the outputstream where the machine is send (like a file for example)
	 * */
    @SuppressWarnings("rawtypes")
	public void generate(Machine machine, File templateFile, OutputStream outputStream) {
        JtwigTemplate template = JtwigTemplate.fileTemplate(templateFile);
        JtwigModel model = JtwigModel.newModel();
        Collection<String> variablesInput = new ArrayList<String>();
        Collection<String> variablesOutput = new ArrayList<String>();
        Collection<String> variablesControl = new ArrayList<String>();
        Collection<String> variablesInternal = new ArrayList<String>();
        Collection<String> invariants = new ArrayList<String>();
        Collection<String> initialisations = new ArrayList<String>();
        Collection<String> senseEvents = new ArrayList<String>();
        Collection<String> reactionEvents = new ArrayList<String>();

        for (Variable variable : machine.getVariables()) {
        	switch (variable.getCategory()) {
                case INPUT:
                    variablesInput.add(variable.getName());
                    if (variable.hasBounds()) {
                    	senseEvents.add(variable.getName()+" : "+variable.getLowerBound()+".."+variable.getUpperBound());
                    } else {
                    	senseEvents.add(variable.getName()+" : BOOL");
                    }
                    break;
                case OUTPUT:
                    variablesOutput.add(variable.getName());
                    if (variable.hasBounds()) {
                    	reactionEvents.add(variable.getName()+" : "+variable.getLowerBound()+".."+variable.getUpperBound());
                    } else {
                    	reactionEvents.add(variable.getName()+" : BOOL");
                    }
                    break;
                case CONTROL:
                    variablesControl.add(variable.getName());
                    break;
                case INTERNAL:
                    variablesInternal.add(variable.getName());
                    break;
            }
            invariants.add(variable.getName()+" : "+variable.getType());
            if (variable.hasBounds()) {
                invariants.add(variable.getName()+" : "+variable.getLowerBound()+".."+variable.getUpperBound());
            }
            initialisations.add(variable.getName()+" := "+variable.getDefaultValue().toString().toUpperCase());
        }

        for (Property property : machine.getProperties()) {
            invariants.add(property.getExpression());
        }

        model.with("name", machine.getName());
        model.with("variablesInput", variablesInput);
        model.with("variablesOutput", variablesOutput);
        model.with("variablesControl", variablesControl);
        model.with("variablesInternal", variablesInternal);
        model.with("invariants", invariants);
        model.with("initialisations", initialisations);
        model.with("senseEvents", senseEvents);
        model.with("reactionEvents", reactionEvents);

        template.render(model, outputStream);
    }
}

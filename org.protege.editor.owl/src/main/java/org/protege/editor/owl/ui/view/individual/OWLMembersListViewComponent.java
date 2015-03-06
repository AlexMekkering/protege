package org.protege.editor.owl.ui.view.individual;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.protege.editor.owl.model.selection.OWLSelectionModelListener;
import org.semanticweb.owlapi.model.*;

/**
 * Author: drummond<br>
 * http://www.cs.man.ac.uk/~drummond/<br><br>
 * <p/>
 * The University Of Manchester<br>
 * Bio Health Informatics Group<br>
 * Date: Oct 14, 2008<br><br>
 *
 * Only shows the members of the currently selected class
 */

/*
 * TODO - this class should probably no longer extend OWLIndividualListViewComponent.
 *        there are too many hacks piling up.  It is not really selectable in the usual sense.
 *        It completely overrides the process changes methods.  It should display anonymous 
 *        individuals.
 */
public class OWLMembersListViewComponent extends OWLIndividualListViewComponent{
	
	private static final long serialVersionUID = -6015526995379146198L;

	private OWLSelectionModelListener l = new OWLSelectionModelListener(){
		public void selectionChanged() throws Exception {
			if (getOWLWorkspace().getOWLSelectionModel().getSelectedObject() instanceof OWLClass){
				refill();
			}
		}
	};


	@Override
	public void initialiseIndividualsView() throws Exception {
		super.initialiseIndividualsView();
		getOWLWorkspace().getOWLSelectionModel().addListener(l);
	}


	protected void refill() {
		individualsInList.clear();
		OWLClass cls = getOWLWorkspace().getOWLSelectionModel().getLastSelectedClass();
		if (cls != null) {
			for(OWLOntology ontology : getOntologies()) {
				for(OWLClassAssertionAxiom ax : ontology.getClassAssertionAxioms(cls)) {
					individualsInList.add(ax.getIndividual());
				}
			}
			if (cls.equals(getOWLModelManager().getOWLDataFactory().getOWLThing())) {
				individualsInList.addAll(getUntypedIndividuals());
			}
		} 
		reset();
	}

	//TODO: do we want to cache this?
	protected Set<OWLNamedIndividual> getUntypedIndividuals() {
		Set<OWLNamedIndividual> untypedIndividuals = new HashSet<>();
		OWLOntology activeOntology = getOWLModelManager().getActiveOntology();
		Set<OWLOntology> importsClosure = activeOntology.getImportsClosure();

		for (OWLNamedIndividual individual : activeOntology.getIndividualsInSignature(true)) {
			Set<OWLClassExpression> types = individual.getTypes(importsClosure);
			if (types == null || types.size() == 0) {
				untypedIndividuals.add(individual);
			}
		}

		return untypedIndividuals;
	}
	
	@Override
	protected void processChanges(List<? extends OWLOntologyChange> changes) {
		refill(); // TODO for now this is ok - but things are bad
	}


	protected List<OWLOntologyChange> dofurtherCreateSteps(OWLIndividual newIndividual) {
		OWLClass cls = getOWLWorkspace().getOWLSelectionModel().getLastSelectedClass();
		if (cls != null){
			OWLAxiom typeAxiom = getOWLModelManager().getOWLDataFactory().getOWLClassAssertionAxiom(cls, newIndividual);
			OWLOntologyChange change = new AddAxiom(getOWLModelManager().getActiveOntology(), typeAxiom);
			return Collections.singletonList(change);
		}
		return new ArrayList<OWLOntologyChange>();
	}


	@Override
	public void disposeView() {
		getOWLWorkspace().getOWLSelectionModel().removeListener(l);
		super.disposeView();
	}
}

package org.protege.editor.owl.model;

import org.semanticweb.owlapi.model.OWLAnnotation;

import java.util.Set;
/*
* Copyright (C) 2007, University of Manchester
*
*
*/

/**
 * Author: drummond<br>
 * http://www.cs.man.ac.uk/~drummond/<br><br>
 * <p/>
 * The University Of Manchester<br>
 * Bio Health Informatics Group<br>
 * Date: Jun 8, 2009<br><br>
 *
 */

// TODO Remove when added to the OWL API
public interface AnnotationContainer {

    Set<OWLAnnotation> getAnnotations();

}

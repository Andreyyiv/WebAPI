package org.ohdsi.webapi.test.entity;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.ohdsi.webapi.test.entity.cohortcharacterization.copy.TestCCCopy;
import org.ohdsi.webapi.test.entity.cohortcharacterization.create.TestCCCreate;
import org.ohdsi.webapi.test.entity.cohortcharacterization.importing.TestCCImport;
import org.ohdsi.webapi.test.entity.cohortdefinition.copy.TestCDCopy;
import org.ohdsi.webapi.test.entity.cohortdefinition.create.TestCDCreate;
import org.ohdsi.webapi.test.entity.conceptset.copy.TestCSCopy;
import org.ohdsi.webapi.test.entity.conceptset.create.TestCSCreate;
import org.ohdsi.webapi.test.entity.estimation.copy.TestEstimationCopy;
import org.ohdsi.webapi.test.entity.estimation.create.TestEstimationCreate;
import org.ohdsi.webapi.test.entity.estimation.importing.TestEstimationImport;
import org.ohdsi.webapi.test.entity.incidencerate.copy.TestIRCopy;
import org.ohdsi.webapi.test.entity.incidencerate.create.TestIRCreate;
import org.ohdsi.webapi.test.entity.pathway.copy.TestPWCopy;
import org.ohdsi.webapi.test.entity.pathway.create.TestPWCreate;
import org.ohdsi.webapi.test.entity.pathway.importing.TestPWImport;
import org.ohdsi.webapi.test.entity.prediction.copy.TestPredictionCopy;
import org.ohdsi.webapi.test.entity.prediction.create.TestPredictionCreate;
import org.ohdsi.webapi.test.entity.prediction.importing.TestPredictionImport;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        TestCCCreate.class,
        TestCCImport.class,
        TestCCCopy.class,
        
        TestCSCreate.class,
        TestCSCopy.class,
        
        TestCDCreate.class,
        TestCDCopy.class,
        
        TestPWCreate.class,
        TestPWImport.class,
        TestPWCopy.class,
        
        TestIRCreate.class,
        TestIRCopy.class,

        TestEstimationCreate.class,
        TestEstimationImport.class,
        TestEstimationCopy.class,

        TestPredictionCreate.class,
        TestPredictionImport.class,
        TestPredictionCopy.class})
public class TestRunner {
}

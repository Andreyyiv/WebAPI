package org.ohdsi.webapi.test.entity.estimation.importing;

import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.ohdsi.analysis.Utils;
import org.ohdsi.webapi.analysis.AnalysisCohortDefinition;
import org.ohdsi.webapi.estimation.Estimation;
import org.ohdsi.webapi.estimation.dto.EstimationDTO;
import org.ohdsi.webapi.estimation.specification.EstimationAnalysisImpl;
import org.ohdsi.webapi.test.entity.estimation.BaseEstimationTestEntity;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.ohdsi.analysis.estimation.design.EstimationTypeEnum.COMPARATIVE_COHORT_ANALYSIS;

public class TestEstimationImport extends BaseEstimationTestEntity {

    @Test
    public void testImportUniqueName() throws Exception {

        //Arrange
        Estimation savedEntity = pleService.getAnalysis(firstSavedDTO.getId());
        EstimationAnalysisImpl exportEntity = pleController.exportAnalysis(savedEntity.getId());
        exportEntity.setName(SOME_UNIQUE_TEST_NAME);

        //Action
        EstimationDTO firstImport = pleController.importAnalysis(exportEntity);

        //Assert
        assertEquals(SOME_UNIQUE_TEST_NAME, firstImport.getName());
    }

    @Test
    public void testImportWithTheSameName() throws Exception {

        //Arrange
        Estimation createdEntity = pleService.getAnalysis(firstSavedDTO.getId());
        EstimationAnalysisImpl exportEntity = pleController.exportAnalysis(createdEntity.getId());

        //Action
        EstimationDTO firstImport = pleController.importAnalysis(exportEntity);
        //reset dto
        exportEntity = pleController.exportAnalysis(createdEntity.getId());
        EstimationDTO secondImport = pleController.importAnalysis(exportEntity);

        //Assert
        assertEquals(NEW_TEST_ENTITY + " (1)", firstImport.getName());
        assertEquals(NEW_TEST_ENTITY + " (2)", secondImport.getName());
    }

    @Test
    public void testImportWhenEntityWithNameExists() throws Exception {

        //Arrange
        Estimation firstCreatedEntity = pleService.getAnalysis(firstSavedDTO.getId());
        EstimationAnalysisImpl firstExportEntity = pleController.exportAnalysis(firstCreatedEntity.getId());

        Estimation secondIncomingEntity = new Estimation();
        secondIncomingEntity.setName(NEW_TEST_ENTITY + " (1)");
        secondIncomingEntity.setType(COMPARATIVE_COHORT_ANALYSIS);
        secondIncomingEntity.setSpecification(PLE_SPECIFICATION);
        //save "New test entity (1)" to DB
        pleController.createEstimation(secondIncomingEntity);

        Estimation thirdIncomingEntity = new Estimation();
        thirdIncomingEntity.setName(NEW_TEST_ENTITY + " (1) (2)");
        thirdIncomingEntity.setType(COMPARATIVE_COHORT_ANALYSIS);
        thirdIncomingEntity.setSpecification(PLE_SPECIFICATION);
        //save "New test entity (1) (2)" to DB
        EstimationDTO thirdSavedDTO = pleController.createEstimation(thirdIncomingEntity);
        Estimation thirdCreatedEntity = pleService.getAnalysis(thirdSavedDTO.getId());
        EstimationAnalysisImpl thirdExportEntity = pleController.exportAnalysis(thirdCreatedEntity.getId());

        //Action
        //import of "New test entity"
        EstimationDTO firstImport = pleController.importAnalysis(firstExportEntity);
        //import of "New test entity (1) (2)"
        EstimationDTO secondImport = pleController.importAnalysis(thirdExportEntity);

        Estimation fourthIncomingEntity = new Estimation();
        fourthIncomingEntity.setName(NEW_TEST_ENTITY + " (1) (2) (2)");
        fourthIncomingEntity.setType(COMPARATIVE_COHORT_ANALYSIS);
        fourthIncomingEntity.setSpecification(PLE_SPECIFICATION);
        //save "New test entity (1) (2) (2)" to DB
        pleController.createEstimation(fourthIncomingEntity);

        //reset dto
        thirdExportEntity = pleController.exportAnalysis(thirdCreatedEntity.getId());
        //import of "New test entity (1) (2)"
        EstimationDTO thirdImport = pleController.importAnalysis(thirdExportEntity);

        //Assert
        assertEquals(NEW_TEST_ENTITY + " (2)", firstImport.getName());
        assertEquals(NEW_TEST_ENTITY + " (1) (2) (1)", secondImport.getName());
        assertEquals(NEW_TEST_ENTITY + " (1) (2) (3)", thirdImport.getName());
    }

    @Test
    public void testImportWhenHashcodesOfCDsAndCSsAreDifferent() throws Exception {

        //Arrange
        File pleFile = new File("src/test/resources/ple-example-for-import.json");
        String pleStr = FileUtils.readFileToString(pleFile, StandardCharsets.UTF_8);

        EstimationAnalysisImpl ple = Utils.deserialize(pleStr, EstimationAnalysisImpl.class);

        //Action
        pleController.importAnalysis(ple);

        cdRepository.findAll().forEach(cd -> {
            cd.setExpression(cd.getExpression().replaceAll("5.0.0", "6.0.0"));
            cdRepository.save(cd);
        });
        EstimationDTO importedEs = pleController.importAnalysis(ple);

        //Assert
        assertEquals("Comparative effectiveness of ACE inhibitors vs Thiazide diuretics as first-line monotherapy for hypertension (1)",
                pleController.getAnalysis(importedEs.getId()).getName());
        EstimationAnalysisImpl importedExpression = pleService.getAnalysisExpression(importedEs.getId());
        List<AnalysisCohortDefinition> cds = importedExpression.getCohortDefinitions();
        assertEquals("New users of ACE inhibitors as first-line monotherapy for hypertension (1)", cds.get(0).getName());
        assertEquals("New users of Thiazide-like diuretics as first-line monotherapy for hypertension (1)", cds.get(1).getName());
        assertEquals("Acute myocardial infarction events (1)", cds.get(2).getName());
        assertEquals("Angioedema events (1)", cds.get(3).getName());
    }
}

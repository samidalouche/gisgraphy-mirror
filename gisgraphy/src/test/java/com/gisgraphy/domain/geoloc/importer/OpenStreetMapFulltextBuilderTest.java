package com.gisgraphy.domain.geoloc.importer;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static junit.framework.Assert.fail;
import static org.easymock.EasyMock.anyObject;
import static org.easymock.EasyMock.expect;
import static org.easymock.classextension.EasyMock.createMock;
import static org.easymock.classextension.EasyMock.replay;
import static org.easymock.classextension.EasyMock.verify;

import java.util.List;

import junit.framework.Assert;

import org.junit.Test;

import com.gisgraphy.domain.repository.IOpenStreetMapDao;
import com.gisgraphy.domain.repository.OpenStreetMapDao;
import com.gisgraphy.domain.valueobject.ImporterStatus;
import com.gisgraphy.domain.valueobject.NameValueDTO;
import com.gisgraphy.service.IInternationalisationService;

public class OpenStreetMapFulltextBuilderTest {

	@Test
	public void testShouldBeSkiped() {
		ImporterConfig importerConfig = new ImporterConfig();
		OpenStreetMapFulltextBuilder openStreetMapFulltextBuilderTobeSkipped = new OpenStreetMapFulltextBuilder();
		openStreetMapFulltextBuilderTobeSkipped.setImporterConfig(importerConfig);

		importerConfig.setOpenstreetmapImporterEnabled(false);
		assertTrue(openStreetMapFulltextBuilderTobeSkipped.shouldBeSkipped());

		importerConfig.setOpenstreetmapImporterEnabled(true);
		assertFalse(openStreetMapFulltextBuilderTobeSkipped.shouldBeSkipped());
	}

	@Test
	public void testSetupShouldCreateTheIndex() {
		OpenStreetMapFulltextBuilder builder = new OpenStreetMapFulltextBuilder();
		IOpenStreetMapDao openStreetMapDao = createMock(IOpenStreetMapDao.class);
		openStreetMapDao.createFulltextIndexes();
		replay(openStreetMapDao);

		IInternationalisationService internationalisationService = createMock(IInternationalisationService.class);
		String localizedString = "localizedString";
		expect(internationalisationService.getString((String) anyObject())).andStubReturn(localizedString);
		replay(internationalisationService);

		builder.setOpenStreetMapDao(openStreetMapDao);
		builder.setInternationalisationService(internationalisationService);

		builder.setup();

		assertEquals(localizedString, builder.getStatusMessage());
		verify(openStreetMapDao);
	}

	@Test
	public void testProcessWhenShouldBeSkipped() {
		OpenStreetMapFulltextBuilder builder = new OpenStreetMapFulltextBuilder() {
			@Override
			public boolean shouldBeSkipped() {
				return true;
			}

			@Override
			protected void setup() {
			}
		};

		builder.process();

		assertEquals(ImporterStatus.SKIPPED, builder.getStatus());
		assertEquals("", builder.getStatusMessage());
	}

	@Test
	public void testProcess() {
		OpenStreetMapFulltextBuilder builder = new OpenStreetMapFulltextBuilder() {

			@Override
			public boolean shouldBeSkipped() {
				return false;
			}
		};
		IOpenStreetMapDao openStreetMapDao = createMock(IOpenStreetMapDao.class);
		openStreetMapDao.createFulltextIndexes();
		expect(openStreetMapDao.countEstimate()).andReturn(400L);
		expect(openStreetMapDao.updateTS_vectorColumnForStreetNameSearchPaginate(0, builder.increment - 1)).andReturn(250);
		expect(openStreetMapDao.updateTS_vectorColumnForStreetNameSearchPaginate(builder.increment, (builder.increment * 2) - 1)).andReturn(200);
		replay(openStreetMapDao);

		IInternationalisationService internationalisationService = createMock(IInternationalisationService.class);
		String localizedString = "localizedString";
		expect(internationalisationService.getString((String) anyObject())).andStubReturn(localizedString);
		replay(internationalisationService);

		builder.setOpenStreetMapDao(openStreetMapDao);
		builder.setInternationalisationService(internationalisationService);

		builder.process();

		assertEquals("", builder.getStatusMessage());
		assertEquals(ImporterStatus.PROCESSED, builder.getStatus());
		assertEquals(builder.getNumberOfLinesToProcess(), builder.getTotalReadLine());
		verify(openStreetMapDao);
	}

	@Test
	public void testRollback() throws Exception {
		OpenStreetMapFulltextBuilder builder = new OpenStreetMapFulltextBuilder();
		List<NameValueDTO<Integer>> dtoList = builder.rollback();
		Assert.assertNotNull(dtoList);
		Assert.assertEquals(0, dtoList.size());
		Assert.assertEquals(0, builder.getNumberOfLinesToProcess());
		Assert.assertEquals(0, builder.getTotalReadLine());
		Assert.assertEquals(0, builder.getReadFileLine());
		Assert.assertEquals(ImporterStatus.WAITING, builder.getStatus());
		Assert.assertEquals("", builder.getStatusMessage());
	}

	@Test
	public void testGetCurrentFileNameShouldReturnTheClassName() {
		OpenStreetMapFulltextBuilder builder = new OpenStreetMapFulltextBuilder();
		builder.setOpenStreetMapDao(new OpenStreetMapDao());
		assertEquals(OpenStreetMapFulltextBuilder.class.getSimpleName(), builder.getCurrentFileName());
	}

	@Test
	public void testResetStatusShouldReset() {
		OpenStreetMapFulltextBuilder builder = new OpenStreetMapFulltextBuilder() {
			@Override
			protected void setup() {
				throw new RuntimeException();
			}
		};
		try {
			builder.process();
			fail("The fulltextbuilder should have throws");
		} catch (RuntimeException ignore) {
		}
		Assert.assertTrue(builder.getStatusMessage().length() > 0);
		Assert.assertEquals(ImporterStatus.ERROR, builder.getStatus());
		builder.resetStatus();
		Assert.assertEquals(0, builder.getNumberOfLinesToProcess());
		Assert.assertEquals(0, builder.getTotalReadLine());
		Assert.assertEquals(0, builder.getReadFileLine());
		Assert.assertEquals(ImporterStatus.WAITING, builder.getStatus());
		Assert.assertEquals("", builder.getStatusMessage());
	}

}

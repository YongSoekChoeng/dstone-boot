package net.dstone.sample.google.service;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;
import net.dstone.sample.google.service.DriveService;

@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
@Slf4j
class DriveServiceTest {
	
	private DriveService driveService;

	@Test
	void testGetFileList() {
		driveService = new DriveService();
		driveService.getFileList("Test");
	}

	@Test
	void testEcho() {
		driveService = new DriveService();
		driveService.echo("Test");
	}

}

package com.isedykh.profiles;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.ThreadLocalRandom;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ProfilesApplicationTests {

	@Value("#{T(java.util.concurrent.ThreadLocalRandom).current().nextInt(20) + ' 0 * * * *' }")
	private String data;

	@Test
	public void contextLoads() {
		System.out.println(data);
	}

}

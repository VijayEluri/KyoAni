package net.hisme.masaki.kyoani;

import static org.junit.Assert.*;
import org.junit.*;

public class AppTest {
	private App app;

	@Before
	public void init() {
		this.app = new App();
	}

	@Ignore
	public void App_li_should_be_an_instance_of_App() {
		assertEquals(this.app, App.li);
	}

}

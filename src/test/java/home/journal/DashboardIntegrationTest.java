package home.journal;

import org.apache.catalina.LifecycleException;
import org.apache.catalina.LifecycleState;
import org.apache.catalina.startup.Tomcat;
import org.apache.commons.io.FileUtils;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.impl.base.exporter.zip.ZipExporterImpl;
import org.jboss.shrinkwrap.resolver.api.maven.archive.importer.MavenImporter;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import javax.servlet.ServletException;
import java.io.File;
import java.io.IOException;

public class DashboardIntegrationTest
{
    private int PORT = 8080;

    private final WebArchive WAR = ShrinkWrap.create(MavenImporter.class)
                                             .loadPomFromFile("pom.xml")
                                             .importBuildOutput()
                                             .as(WebArchive.class);

    private final String WEB_APP_EXTENSION = ".war";
    private final String WEB_APP_DIR = System.getProperty("java.io.tmpdir");
    private final String WEB_APP_NAME = WAR.getName().substring(0, WAR.getName().length() - WEB_APP_EXTENSION.length());

    private Tomcat tomcat;

    @Before
    public void setup() throws IOException, ServletException, LifecycleException
    {
        tomcat = new Tomcat();

        tomcat.setPort(PORT);
        tomcat.setBaseDir(WEB_APP_DIR);
        tomcat.getHost().setAppBase(WEB_APP_DIR);
        tomcat.getHost().setAutoDeploy(true);
        tomcat.getHost().setDeployOnStartup(true);

        final File webApp = new File(WEB_APP_DIR, WEB_APP_NAME);
        final File oldWebApp = new File(webApp.getAbsolutePath());
        FileUtils.deleteDirectory(oldWebApp);

        new ZipExporterImpl(WAR).exportTo(new File(WEB_APP_DIR + File.separator + WAR.getName()), true);

        tomcat.addWebapp("/", webApp.getAbsolutePath());
        tomcat.start();
    }

    @After
    public void cleanup() throws LifecycleException
    {
        if(tomcat.getServer() != null && tomcat.getServer().getState() != LifecycleState.DESTROYED) {
            if (tomcat.getServer().getState() != LifecycleState.STOPPED) {
                tomcat.stop();
            }

            tomcat.destroy();
        }
    }

    @Test
    public void dashboardTest()
    {
        final String url = "http://localhost:" + PORT;
        final RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

        Assert.assertTrue(response.getBody().contains("Tweet Dashboard"));
    }
}

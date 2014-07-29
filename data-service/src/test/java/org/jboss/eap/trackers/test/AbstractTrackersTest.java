/**
 * @author <a href="mailto:lgao@redhat.com">Lin Gao </a>
 */
package org.jboss.eap.trackers.test;

import javax.ejb.EJB;
import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.eap.trackers.data.DataService;
import org.jboss.eap.trackers.data.db.CDIResources;
import org.jboss.eap.trackers.data.db.DBDataService;
import org.jboss.eap.trackers.data.db.RestService;
import org.jboss.eap.trackers.data.db.SetString;
import org.jboss.eap.trackers.data.db.TrackerRestApplication;
import org.jboss.eap.trackers.model.Product;
import org.jboss.logging.Logger;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.runner.RunWith;

/**
 * @author lgao
 *
 */
@RunWith(Arquillian.class)
public abstract class AbstractTrackersTest {

	@Deployment
	public static Archive<?> createTestArchive() {
		return ShrinkWrap
				.create(JavaArchive.class, "test.jar")
				.addAsResource("META-INF/test-persistence.xml",
						"META-INF/persistence.xml")
				.addAsResource("META-INF/orm.xml", "META-INF/orm.xml")
				.addAsResource("artis.txt", "artis.txt")
				.addAsResource("comps.txt", "comps.txt")
				.addAsResource("import.sql", "import.sql")
				.addPackage(DataService.class.getPackage())
				.addClass(CDIResources.class)
				.addClass(RestService.class)
				.addClass(SetString.class)
				.addClass(TrackerRestApplication.class)
				.addClass(DBDataService.class)
				.addPackage(Product.class.getPackage())
				.addPackage(AbstractTrackersTest.class.getPackage())
				.addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
	}

	@EJB
	DataService dataService;
	
	@EJB(name = "TrackerBean")
	TrackerBean tracker;

	@Inject
	Logger log;
}

package ut.org.catrobat.confluence.services.imp;

import com.atlassian.confluence.user.UserAccessor;
import com.atlassian.activeobjects.external.ActiveObjects;
import net.java.ao.EntityManager;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import com.atlassian.activeobjects.test.TestActiveObjects;
import java.util.ArrayList;
import java.util.List;
import net.java.ao.test.jdbc.Data;
import net.java.ao.test.jdbc.DatabaseUpdater;
import net.java.ao.test.junit.ActiveObjectsJUnitRunner;
import org.catrobat.confluence.activeobjects.Team;
import org.catrobat.confluence.services.TeamService;
import org.catrobat.confluence.services.imp.TeamServiceImpl;
import org.junit.Assert;
import org.junit.Rule;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnit;

@RunWith(ActiveObjectsJUnitRunner.class)
@Data(TeamServiceImplTest.MyDatabaseUpdater.class)

public class TeamServiceImplTest {

  private EntityManager entityManager;
	private TeamService service;
  private ActiveObjects ao;
  private UserAccessor userAccessor;
  private List<String> groups;
  private static Team catroid, html5, drone;
  
  @Rule public org.mockito.junit.MockitoRule mockitoRule = MockitoJUnit.rule();
  @Before
	public void setUp() throws Exception
	{
    groups = new ArrayList<String>();
    groups.add("Administrators");
    groups.add("Catroid");
    groups.add("catroid");
    groups.add("Confluence-Administrators");
    groups.add("Confluence");
    groups.add("HTML5-Coordinators");

    userAccessor = Mockito.mock(UserAccessor.class);
		assertNotNull(entityManager);
		ao = new TestActiveObjects(entityManager);
		service = new TeamServiceImpl(ao, userAccessor);
	}

	public static class MyDatabaseUpdater implements DatabaseUpdater {

		@Override
		public void update(EntityManager em) throws Exception {
			em.migrate(Team.class);
      catroid = em.create(Team.class);
      catroid.setTeamName("catroid");
      catroid.save();
      
      html5 = em.create(Team.class);
      html5.setTeamName("html5");
      html5.save();
      
      drone = em.create(Team.class);
      drone.setTeamName("drone");
      drone.save();
		}
	}
	
	@Test
	public void testGetTeamsOfUser() throws Exception
	{
    //arrange
    String userName = "user_x";
    Mockito.when(userAccessor.getGroupNamesForUserName(userName)).thenReturn(groups);
    Team[] expectedTeams = new Team[2];
    expectedTeams[0] = catroid;
    expectedTeams[1] = html5;
    
    //act
    Team[] teams = service.getTeamsOfUser(userName);
    
    //assert
    Assert.assertArrayEquals(expectedTeams, teams);
    
	}
}

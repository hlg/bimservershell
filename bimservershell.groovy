@Grapes(
    @Grab(group='org.opensourcebim', module='bimserverclientlib', version='1.5.76')
)

import org.bimserver.client.BimServerClient;
import org.bimserver.client.json.JsonBimServerClientFactory;
import org.bimserver.interfaces.objects.SProject;
import org.bimserver.shared.ChannelConnectionException;
import org.bimserver.shared.UsernamePasswordAuthenticationInfo;
import org.bimserver.shared.exceptions.BimServerClientException;
import org.bimserver.shared.exceptions.PublicInterfaceNotFoundException;
import org.bimserver.shared.exceptions.ServiceException;

import org.codehaus.groovy.tools.shell.Groovysh
import org.codehaus.groovy.tools.shell.IO 

JsonBimServerClientFactory factory = new JsonBimServerClientFactory("http://localhost:8080")
BimServerClient client = factory.create(new UsernamePasswordAuthenticationInfo("admin@bimserver.org", "admin"))

SProject project = cient.serviceInterface.getProjectByPoid(args[1])
IfcModelInterface model = client.getModel(project, project.lastRevisionId, true, false) // load deep, no geometry? interface might have changed, this one private now

shell = new Groovysh([model: model] as Binding, new IO())
shell.run()
   
// https://github.com/groovy/groovy-core/tree/master/subprojects/groovy-groovysh/src/main/groovy/org/codehaus/groovy/tools/shell/commands



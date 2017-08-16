@Grapes([
    @Grab(group='org.opensourcebim', module='bimserverclientlib', version='1.5.76'),
    @Grab(group='org.opensourcebim', module='pluginbase', version='1.5.76')
])

import org.bimserver.client.BimServerClient;
import org.bimserver.client.json.JsonBimServerClientFactory;
import org.bimserver.interfaces.objects.SProject;
import org.bimserver.shared.UsernamePasswordAuthenticationInfo;
import org.bimserver.emf.IfcModelInterface;

import org.codehaus.groovy.tools.shell.Groovysh
import org.codehaus.groovy.tools.shell.IO 

JsonBimServerClientFactory factory = new JsonBimServerClientFactory("http://localhost:8082")
BimServerClient client = factory.create(new UsernamePasswordAuthenticationInfo("admin@localhost", "admin"))
SProject project = client.serviceInterface.getProjectsByName(args[0])[0]

IfcModelInterface model = client.getModel(project, project.lastRevisionId, true, false) // load deep, no geometry? interface might have changed, this one private now

println "loading project $project.oid into shell context"
shell = new Groovysh([model: model] as Binding, new IO())
shell.run('')

   
// https://github.com/groovy/groovy-core/tree/master/subprojects/groovy-groovysh/src/main/groovy/org/codehaus/groovy/tools/shell/commands



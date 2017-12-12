import org.bimserver.client.BimServerClient
import org.bimserver.client.json.JsonBimServerClientFactory
import org.bimserver.interfaces.objects.SProject
import org.bimserver.shared.UsernamePasswordAuthenticationInfo
import org.bimserver.emf.IfcModelInterface
import org.fusesource.jansi.Ansi
import org.fusesource.jansi.AnsiConsole
import org.codehaus.groovy.tools.shell.AnsiDetector
import org.codehaus.groovy.tools.shell.Groovysh
import org.codehaus.groovy.tools.shell.IO 

def cli = new CliBuilder(usage: 'groovy bimservershell.groovy')
cli.H(optionalArg: true, longOpt: 'host', args: 1, argName: 'url', 'use BimServer at given location, defaults to http://localhost:8082')
cli.U(optionalArg: true, longOpt: 'user', args: 1, argName: 'email', 'use user to login to BimServer, defaults to admin@localhost')
cli.P(optionalArg: true, longOpt: 'password', args: 1, argName: 'pass', 'use password to login to BimServer, defaults to admin')
cli.M(optionalArg: true, longOpt: 'model', args: 1, argName: 'project', 'load project with given name')
cli.R(optionalArg: true, longOpt: 'revision', args: 1, argName: 'revision', 'load given revision, defaults to last revision')
cli.C(optionalArg: true, longOpt: 'trustedCert', args: 1, argName: 'trustedCert', 'trusted self-signed certificate')
def options = cli.parse(args)

if(options){
    println "loading $options.M"
    URL trustedCert = null
    if (options.C) {
        File certFile = new File(options.C)
        if(certFile.exists()) trustedCert = certFile.toURI().toURL()
    }
    JsonBimServerClientFactory factory = new JsonBimServerSSLClientFactory(options.H ?: "http://localhost:8082", trustedCert)
    if(factory){
        BimServerClient client = factory.create(new UsernamePasswordAuthenticationInfo(options.U ?: "admin@localhost",  options.P ?: "admin"))
        IfcModelInterface model = null
        if(options.M){
            SProject project = client.serviceInterface.getProjectsByName(options.M)[0] // TODO getAllProjects and do regex matching
            println "loading project $project.oid into shell context"
            model = client.getModel(project, project.revisions[options.R ?: 0], true, false)
        }
        AnsiConsole.systemInstall()
        Ansi.setDetector(new AnsiDetector())
        shell = new Groovysh([model: model, client: client] as Binding, new IO())
        shell.run('')

    }
}

// https://github.com/groovy/groovy-core/tree/master/subprojects/groovy-groovysh/src/main/groovy/org/codehaus/groovy/tools/shell/commands

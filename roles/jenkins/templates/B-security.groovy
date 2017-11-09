#!groovy
import hudson.security.*
import jenkins.model.*
import jenkins.security.s2m.AdminWhitelistRule

def instance = Jenkins.getInstance()
def hudsonRealm = new HudsonPrivateSecurityRealm(false)
def users = hudsonRealm.getAllUsers()
users_s = users.collect { it.toString() }

// Create the admin user account if it doesn't already exist.
if ("{{ jenkins_admin_username }}" in users_s) {
    println "Admin user already exists - updating password"
    def user = hudson.model.User.get('{{ jenkins_admin_username }}');
    def password = hudson.security.HudsonPrivateSecurityRealm.Details.fromPlainPassword('{{ jenkins_admin_password }}')
    user.addProperty(password)
    user.save()
}
else {
    println "--> creating local admin user"
    hudsonRealm.createAccount('{{ jenkins_admin_username }}', '{{ jenkins_admin_password }}')
    instance.setSecurityRealm(hudsonRealm)

    def strategy = new FullControlOnceLoggedInAuthorizationStrategy()
    strategy.setAllowAnonymousRead(false)
    instance.setAuthorizationStrategy(strategy)
    // Disable remote CLI
    // https://jenkins.io/doc/book/managing/cli/
    instance.getDescriptor("jenkins.CLI").get().setEnabled(false)
    // Enable Slave to Master Access
    // https://wiki.jenkins.io/display/JENKINS/Slave+To+Master+Access+Control
    instance.getInjector().getInstance(AdminWhitelistRule.class).setMasterKillSwitch(false)
    // Enable CSRF protection
    // https://wiki.jenkins.io/display/JENKINS/CSRF+Protection
    instance.setCrumbIssuer(new DefaultCrumbIssuer(true))
    instance.save()
}

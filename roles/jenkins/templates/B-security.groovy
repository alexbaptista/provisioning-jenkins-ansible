#!groovy
import hudson.security.*
import hudson.security.csrf.DefaultCrumbIssuer
import jenkins.model.*
import jenkins.security.s2m.AdminWhitelistRule
import java.util.logging.Logger

def logger = Logger.getLogger("")
def instance = Jenkins.getInstance()
def hudsonRealm = new HudsonPrivateSecurityRealm(false)
def users = hudsonRealm.getAllUsers()
users_s = users.collect { it.toString() }

// Create the admin user account if it doesn't already exist.
if ("{{ jenkins_admin_username }}" in users_s) {
    logger.info("user {{ jenkins_admin_username }} already exists - updating password")
    def user = hudson.model.User.get('{{ jenkins_admin_username }}');
    def password = hudson.security.HudsonPrivateSecurityRealm.Details.fromPlainPassword('{{ jenkins_admin_password }}')
    user.addProperty(password)
}
else {
    logger.info("Creating local user {{ jenkins_admin_username }}")
    hudsonRealm.createAccount('{{ jenkins_admin_username }}', '{{ jenkins_admin_password }}')
    instance.setSecurityRealm(hudsonRealm)
}

// Setting strategy for logon
logger.info("Setting strategy authentication for login")
def strategy = new FullControlOnceLoggedInAuthorizationStrategy()
strategy.setAllowAnonymousRead(false)
instance.setAuthorizationStrategy(strategy)

// Disable remote CLI
// https://jenkins.io/doc/book/managing/cli/
logger.info("Disabling remote Jenkins CLI")
instance.getDescriptor("jenkins.CLI").get().setEnabled(false)

// Enable Slave to Master Access
// https://wiki.jenkins.io/display/JENKINS/Slave+To+Master+Access+Control
logger.info("Enable slave to Master Access")
instance.getInjector().getInstance(AdminWhitelistRule.class).setMasterKillSwitch(false)

// Enable CSRF protection
// https://wiki.jenkins.io/display/JENKINS/CSRF+Protection
logger.info("Enabling CSRF protection with crumb default")
instance.setCrumbIssuer(new DefaultCrumbIssuer(true))

// Save configurations
instance.save()

# provisioning-jenkins-ansible

## About

This is a simple example Ansible-playbook,

## Requirements

* Debian Stretch (codename "9")
* Ansible (2.4 or newer)
* SSH connection

## Usage

* Clone this repo
```
git clone https://github.com/cs-alex-baptista/provisioning-jenkins-ansible.git
```

* Configure user and host in **hosts** file
```
[debian]
127.0.0.1:2222 ansible_user=root ansible_become=yes
```

* Configure jenkins options in **group_vars/debian.yml**

```
jenkins_home: /home/jenkins
jenkins_admin_username: admin
jenkins_admin_password: admin
```

* Execute the ansible playbook
```
ansible-playbook -i hosts playbook/site.yml -v
```

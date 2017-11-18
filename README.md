# provisioning-jenkins-ansible

## About

This is a simple Ansible-playbook example, provision a Jenkins in a Docker container, with NGINX as reverse-proxy.

## Requirements

* AWS-CLI
* Ansible (2.4 or newer)

## Usage

* Clone this repo
```
git clone https://github.com/cs-alex-baptista/provisioning-jenkins-ansible.git
```

* Configure AWS and Jenkins options in **group_vars/all.yml**

```
# AWS
aws_access_key: # Use environment variables - http://docs.aws.amazon.com/cli/latest/userguide/cli-environment.html
aws_secret_key: # Use environment variables - http://docs.aws.amazon.com/cli/latest/userguide/cli-environment.html
key_name: lab-ec2
aws_region: us-east-1
vpc_id: vpc-6a29fe0c
vpc_subnet_id: subnet-5092057d
ami_id: ami-71b7750b #AMI Debian Stretch 9
instance_type: t2.large
security_group_name: jenkins_stack_rules
# AWS EC2
ansible_ssh_private_key_file: /Users/alexbaptista/.ssh/lab-ec2.pem
ansible_user: admin
ansible_become_method: sudo
# Jenkins Config
jenkins_home: /home/jenkins
jenkins_admin_username: admin
jenkins_admin_password: admin
jenkins_plugins: cloudbees-folder antisamy-markup-formatter and others
```

* Execute the ansible playbook to create AWS Machine
```
ansible-playbook -i ec2.py playbook/site.yml
```

* Execute the ansible playbook to configure NGINX + Docker + Jenkins
```
ansible-playbook -i ec2.py playbook/jenkins_stack.yml
```

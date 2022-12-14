pipeline {
    agent any

    tools {
        maven 'Maven-3.8.6'
        jdk 'jdk17'
    }

    stages {
        stage("build") {
            steps {
                dir('slr_spot_backend') {
                    sh 'echo ${USER}'
                    sh 'mvn -Dmaven.test.failure.ignore=true clean install'
                }
            }
        }

        stage("test") {
            steps {
                dir('slr_spot_backend') {
                    sh 'mvn test'
                }
            }
        }

        stage('SonarQube Analysis') {
            when {
                branch 'main'
            }
            steps {
                dir('slr_spot_backend') {
                    withSonarQubeEnv(installationName: 'sonarqube') {
                        sh 'mvn sonar:sonar -Dsonar.projectKey=slr-spot'
                    }
                }
            }
        }

        stage('Build docker image') {
            when {
                branch 'develop'
            }
			steps {
                dir('slr_spot_backend') {
				    sh 'docker build -t vdannys/slrapp:lts .'
                }
			}
		}

		stage('Push docker image') {
            when {
                branch 'develop'
            }
			steps {
                withCredentials([usernamePassword(credentialsId: 'docker-credentials', usernameVariable: 'USERNAME', passwordVariable: 'PASSWORD')]) {
                    dir('slr_spot_backend') {
                        sh 'docker login -u $USERNAME -p $PASSWORD'
                        sh 'docker push vdannys/slrapp:lts'
                    }
                }
			}
		}

        stage("deploy") {
            when {
                branch 'main'
            }
            steps {
                echo 'Deploying to servers....'
            }
        }
    }
}
from fabric.api import run, local, put, env
env.warn_only=True

def buildAssets():
    local("gulp")

def buildApp():
    local("sbt universal:packageZipTarball")

def killCurrent():
    run("kill $(pgrep -f books-summary-website)")

def removeOld():
    run("rm -rf /var/app/books-summary-website-1.0")

def uploadAssets():
    put("web/assets", "/root")

def deployAssets():
    run("cp -R /root/assets/* $BOOKSUM_ASSETS_PATH")

def uploadPackage():
    put("target/universal/books-summary-website-1.0.tgz", "/root")

def deployPackage():
    run("tar -zxf /root/books-summary-website-1.0.tgz -C /var/app")

def runApp():
    run("/var/app/books-summary-website-1.0/bin/books-summary-website > /var/log/book-summary-website.log 2>&1 &", pty=False)

def deploy():
    buildAssets()
    buildApp()
    uploadAssets()
    uploadPackage()
    killCurrent()
    removeOld()
    deployAssets()
    deployPackage()
    runApp()

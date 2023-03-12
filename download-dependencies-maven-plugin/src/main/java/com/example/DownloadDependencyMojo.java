package com.example;

import org.apache.maven.artifact.handler.DefaultArtifactHandler;
import org.apache.maven.model.Dependency;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.*;
import org.apache.maven.project.MavenProject;
import org.eclipse.aether.RepositorySystem;
import org.eclipse.aether.RepositorySystemSession;
import org.eclipse.aether.artifact.Artifact;
import org.eclipse.aether.artifact.DefaultArtifact;
import org.eclipse.aether.repository.RemoteRepository;
import org.eclipse.aether.resolution.ArtifactRequest;
import org.eclipse.aether.resolution.ArtifactResolutionException;
import org.eclipse.aether.resolution.ArtifactResult;

import java.util.List;

@Mojo(name = "add-dependency", defaultPhase = LifecyclePhase.PROCESS_RESOURCES/*, requiresDependencyResolution = ResolutionScope.COMPILE*/)
public class DownloadDependencyMojo extends AbstractMojo {

    @Parameter(defaultValue = "${project}", readonly = true)
    private MavenProject project;

    @Component
    private RepositorySystem repoSystem;

    @Parameter(defaultValue = "${repositorySystemSession}", readonly = true)
    private RepositorySystemSession repoSession;

    @Parameter(defaultValue = "${project.remoteProjectRepositories}", readonly = true)
    private List<RemoteRepository> remoteRepos;

    @Parameter(property = "groupId", required = true)
    private String groupId;

    @Parameter(property = "artifactId", required = true)
    private String artifactId;

    @Parameter(property = "version", required = true)
    private String version;

    @Parameter(property = "type", defaultValue = "jar")
    private String type;


    public void execute() throws MojoExecutionException {

        Artifact aetherArtifact = new DefaultArtifact(groupId, artifactId, type, version);

        ArtifactRequest artifactRequest = new ArtifactRequest();
        artifactRequest.setArtifact(aetherArtifact);
        artifactRequest.setRepositories(remoteRepos);

        try {
            ArtifactResult aetherArtifactResult = repoSystem.resolveArtifact(repoSession, artifactRequest);
            Artifact resolvedAetherArtifact = aetherArtifactResult.getArtifact();

            getLog().info("Resolved aetherArtifact: " + resolvedAetherArtifact.getFile().getPath());

            project.addCompileSourceRoot(resolvedAetherArtifact.getFile().getAbsolutePath());
            org.apache.maven.artifact.DefaultArtifact defaultArtifact = toMavenDefaultArtifact(resolvedAetherArtifact, "compile");
            project.addAttachedArtifact(defaultArtifact);

            Dependency compileScopeDependency = toDependency(resolvedAetherArtifact, "compile");
            project.getDependencies().add(compileScopeDependency);

            getLog().info("added to project: " + resolvedAetherArtifact.getFile().getPath());
        } catch (ArtifactResolutionException e) {
            throw new MojoExecutionException("failed to download dependencies", e);
        }
    }

    public static Dependency toDependency(Artifact aetherArtifact, String scope){
        Dependency dependency = new Dependency();
        dependency.setGroupId( aetherArtifact.getGroupId() );
        dependency.setArtifactId( aetherArtifact.getArtifactId() );
        dependency.setVersion(aetherArtifact.getVersion() );
        dependency.setScope( scope );
        dependency.setClassifier( aetherArtifact.getClassifier() );
        return dependency ;
    }

    public static org.apache.maven.artifact.DefaultArtifact toMavenDefaultArtifact(Artifact aetherArtifact, String scope){

        DefaultArtifactHandler handler  = new DefaultArtifactHandler() ;

        org.apache.maven.artifact.DefaultArtifact mavenArtifact = new org.apache.maven.artifact.DefaultArtifact(
                aetherArtifact.getGroupId(),
                aetherArtifact.getArtifactId(),
                aetherArtifact.getVersion(),
                scope,
                aetherArtifact.getExtension(),
                aetherArtifact.getClassifier(),
                handler
        );

        return mavenArtifact ;
    }
}

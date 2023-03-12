package com.example;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;

@Mojo( name = "hello", defaultPhase = LifecyclePhase.PROCESS_SOURCES )
public class HelloMojo extends AbstractMojo
{


    public void execute() throws MojoExecutionException
    {
        System.out.println("hello maven plugin");
    }
}

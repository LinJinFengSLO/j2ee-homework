package com.asafandben.bl.core_entities;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement(name="Tasks")
public class Tasks
{
  private List<Task> tasks = new ArrayList<Task>();

  @XmlElement(name="Task")
  public List<Task> getTasks()
  {
    return tasks;
  }

  public void setTasks(List<Task> tasks)
  {
    this.tasks = tasks;
  }
}

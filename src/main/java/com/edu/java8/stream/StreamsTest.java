package com.edu.java8.stream;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Test;

/**
 * 
 * @author Administrator
 */
public class StreamsTest {
	private enum Status {
		OPEN, CLOSED
	};

	public static final class Task {
		private final Status status;
		private final Integer points;

		Task(final Status status, final Integer points) {
			this.status = status;
			this.points = points;
		}

		public Integer getPoints() {
			return points;
		}

		public Status getStatus() {
			return status;
		}

		@Override
		public String toString() {
			return String.format("[%s, %d]", status, points);
		}
	}

	@Test
	public void test_stream_collection() throws Exception {
		final Collection<Task> tasks = Arrays.asList(new Task(Status.OPEN, 5), new Task(Status.OPEN, 13), new Task(Status.CLOSED, 8));

		// Calculate total points of all active tasks using sum()
		final long totalPointsOfOpenTasks = tasks.stream().filter(task -> task.getStatus() == Status.OPEN).mapToInt(Task::getPoints).sum();
		System.out.println("Total points: " + totalPointsOfOpenTasks);

		// Calculate total points of all tasks
		final double totalPoints = tasks.stream().parallel().map(task -> task.getPoints()) // or map( Task::getPoints )
				.reduce(0, Integer::sum);

		System.out.println("Total points (all tasks): " + totalPoints);

		// Group tasks by their status
		final Map< Status, List< Task > > map = tasks
		    .stream()
		    .collect( Collectors.groupingBy( Task::getStatus ) );
		System.out.println( map );
		
		// Calculate the weight of each tasks (as percent of total points) 
		final Collection< String > result = tasks
		    .stream()                                        // Stream< String >
		    .mapToInt( Task::getPoints )                     // IntStream
		    .asLongStream()                                  // LongStream
		    .mapToDouble( points -> points / totalPoints )   // DoubleStream
		    .boxed()                                         // Stream< Double >
		    .mapToLong( weigth -> ( long )( weigth * 100 ) ) // LongStream
		    .mapToObj( percentage -> percentage + "%" )      // Stream< String> 
		    .collect( Collectors.toList() );                 // List< String > 
		         
		System.out.println( result );
	}
	
	@Test
	public void test_stream_read_file() throws Exception{
		String filename = "D:\\Users\\Administrator\\git\\coreJava\\file\\add.txt";
		final Path path = new File( filename ).toPath();
		try( Stream< String > lines = Files.lines( path, StandardCharsets.UTF_8 ) ) {
		    lines.onClose( () -> System.out.println("Done!") ).forEach( System.out::println );
		}
	}
}
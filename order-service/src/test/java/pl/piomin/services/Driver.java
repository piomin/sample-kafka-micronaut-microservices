package pl.piomin.services;

public class Driver {

	private Long id;
	private String name;

	Long getId() {
		return id;
	}

	void setId(Long id) {
		this.id = id;
	}

	String getName() {
		return name;
	}

	void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "Driver{" +
				"id=" + id +
				", name='" + name + '\'' +
				'}';
	}

}

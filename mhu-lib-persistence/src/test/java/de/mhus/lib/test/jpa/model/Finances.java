/**
 * Copyright 2018 Mike Hummel
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.mhus.lib.test.jpa.model;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import de.mhus.lib.jpa.JpaComfortable;

public class Finances extends JpaComfortable {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;
	private long store;
	private double activa;
	private double passiva;

	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}

	public double getActiva() {
		return activa;
	}
	public void setActiva(double activa) {
		this.activa = activa;
	}

	public double getPassiva() {
		return passiva;
	}
	public void setPassiva(double passiva) {
		this.passiva = passiva;
	}

	public void setStore(long shop) {
		this.store = shop;
	}
	public long getStore() {
		return store;
	}

}

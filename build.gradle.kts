import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	id("org.springframework.boot") version "2.6.7"
	id("io.spring.dependency-management") version "1.0.11.RELEASE"
	id("io.swagger.core.v3.swagger-gradle-plugin") version "2.2.0"
	kotlin("jvm") version "1.6.21"
	kotlin("plugin.allopen") version "1.4.32"
	kotlin("plugin.spring") version "1.6.21"
	kotlin("plugin.jpa") version "1.6.21"
	kotlin("kapt") version "1.6.21"
}

group = "it.giovi"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_1_8

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-security")
	implementation("org.springframework.boot:spring-boot-starter-validation")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-configuration-processor")
	implementation("org.springdoc:springdoc-openapi-ui:1.6.8")
	implementation("io.jsonwebtoken:jjwt:0.9.1")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
	implementation("org.mapstruct:mapstruct:1.5.0.Final")
	implementation("com.linecorp.kotlin-jdsl:hibernate-kotlin-jdsl:2.0.2.RELEASE")
	implementation("org.hibernate:hibernate-core")
	implementation("org.postgresql:postgresql:42.3.4")
	kapt("org.hibernate:hibernate-jpamodelgen")
	compileOnly("org.hibernate:hibernate-jpamodelgen")
	annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
	annotationProcessor("org.mapstruct:mapstruct-processor:1.5.0.Final")
	annotationProcessor("org.hibernate:hibernate-jpamodelgen")
	kapt("org.mapstruct:mapstruct-processor:1.5.0.Final")

	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.springframework.security:spring-security-test")
}


tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs = listOf("-Xjsr305=strict")
		jvmTarget = "1.8"
	}
}


tasks.withType<Test> {
	useJUnitPlatform()
}


allOpen {
	annotation("javax.persistence.Entity")
	annotation("javax.persistence.Embeddable")
	annotation("javax.persistence.MappedSuperclass")
}

describe('Login spec', () => {
  // Avant chaque test, on charge le fichier fixture
  beforeEach(function () {
    cy.fixture('teachers.json').as('teachersData');
    cy.fixture('sessions.json').as('sessionsData');
  });

  it('Login successful', function () {
    // Vérifiez que les données sont chargées correctement
    cy.log('Users data loaded:', this.teachersData);
    expect(this.teachersData).to.have.length.greaterThan(0);
  });

  it('Login successful with user selection', function () { // Utilisez une fonction normale
    cy.visit('/login');

    cy.intercept('POST', '/api/auth/login', {
      body: {
        id: 1,
        username: 'userName',
        firstName: 'firstName',
        lastName: 'lastName',
        admin: true
      },
    });

    // Utilisez les données de la fixture pour l'interception
    cy.intercept('GET', '/api/teacher', {
      body: this.teachersData
    });

    cy.intercept(
      {
        method: 'GET',
        url: '/api/session',
      },
      []).as('session');

    cy.intercept('POST', '/api/session', {
      status: 200,
    });

    cy.intercept( 'GET', '/api/session',{
      body:this.sessionsData
    });

    // Remplir les champs de connexion
    cy.get('input[formControlName=email]').type("yoga@studio.com");
    cy.get('input[formControlName=password]').type(`${"test!1234"}{enter}{enter}`);

    cy.url().should('include', '/sessions');

    cy.get('button[routerlink="create"]').click();

    cy.url().should('include', '/sessions/create');

    cy.get('input[formControlName=name]').type("cypress session");
    cy.get('input[formControlName=date]').type("2025-01-25");

    // Sélectionner un utilisateur dans le menu déroulant
    cy.get('mat-select[formControlName=teacher_id]').click().then(() => {
      // Vérifiez que les données sont présentes dans l'interface
      cy.log('First user in fixture:', this.teachersData[0]); // Débogage
      cy.get('.cdk-overlay-container .mat-select-panel .mat-option-text')
        .should('contain', this.teachersData[0].firstName);

      // Cliquez sur le premier utilisateur
      cy.get(`.cdk-overlay-container .mat-select-panel .mat-option-text:contains(${this.teachersData[0].firstName})`)
        .first().click().then(() => {
        cy.get('[formControlName=teacher_id]').contains(this.teachersData[0].firstName);
      });
    });

    cy.get('textarea[formControlName=description]')
      .type("Session avec Anne Pamanké, car Larry Golade est absent, pour faire du yoga Dougou");

    cy.get('button[type=submit]').click();

    cy.url().should('include', '/sessions');
  });
});

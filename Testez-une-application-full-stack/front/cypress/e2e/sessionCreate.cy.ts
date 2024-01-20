describe('Sessions Create spec', () => {
  beforeEach(() => {
    cy.intercept('GET', '/api/session', {
      fixture: 'sessions.json',
    }).as('sessionsApiCallGet');

    cy.intercept('GET', '/api/teacher', {
      fixture: 'teachers.json',
    }).as('teacherApiCallAll');

    cy.intercept('POST', '/api/session', {
      fixture: 'session.json',
    }).as('sessionApiCallPost');
  });

  const loginUrl = '/login';
  const sessionUrl = '/sessions';
  const createSessionUrl = '/sessions/create';

  const login = () => {
    cy.get('input[formControlName=email]').type('test@test.com');
    cy.get('input[formControlName=password]').type(
      `${'password'}{enter}{enter}`
    );
  };

  const connectAsAdmin = () => {
    cy.visit(loginUrl);

    cy.intercept('POST', '/api/auth/login', {
      body: {
        id: 1,
        username: 'userName',
        firstName: 'firstName',
        lastName: 'lastName',
        admin: true,
      },
    });

    login();
    cy.url().should('include', sessionUrl);
  };

  const sessionsPageViewOnAdmin = () => {
    connectAsAdmin();
    cy.get('mat-card.item').should('have.length', 3);
  };

  const createSessionPageView = () => {
    sessionsPageViewOnAdmin();

    cy.get('button[routerlink="create"]').click();
    cy.url().should('include', createSessionUrl);
  };

  const clickOnFormElem = () => {
    cy.get('input[formControlName=name]').click();
    cy.get('input[formControlName=date]').click();
    cy.get('textarea[formControlName=description]').click();
    cy.get('input[formControlName=name]').click();
  };

  const type = (name: string, date: string, description: string) => {
    cy.get('input[formControlName=name]').type(name);
    cy.get('input[formControlName=date]').type(date);
    cy.get('textarea[formControlName=description]').type(description);
  };

  const submitShouldBeDisable = () => {
    cy.get('button.mat-raised-button:contains("Save")')
      .should('exist')
      .should('be.disabled');
  };

  const submit = () => {
    cy.get('button.mat-raised-button:contains("Save")').click();
  };

  const shouldHaveFormInitCorrect = () => {
    cy.get('input[formControlName=name]').should('exist');
    cy.get('input[formControlName=date]').should('exist');
    cy.get('textarea[formControlName=description]').should('exist');
    cy.get('mat-select[formcontrolname="teacher_id"]').should('exist');

    submitShouldBeDisable();
  };

  const invalidTextCreateSessionForm = () => {
    clickOnFormElem();
    cy.get('mat-form-field.ng-invalid').should('have.length', 4);
    submitShouldBeDisable();
  };

  const validTextCreateSessionForm = () => {
    type('name', '2024-01-01', 'description');
    cy.get('mat-form-field.ng-invalid').should('have.length', 1);
    submitShouldBeDisable();
  };

  const validDropdownSelectionCreateSessionForm = () => {
    cy.get('mat-select[formcontrolname="teacher_id"]').click();
    cy.get('mat-option').should('have.length', 3);
    cy.get('mat-option').first().click();
    cy.contains('span.mat-select-min-line', 'firstName lastName').should(
      'exist'
    );
  };

  const correctFormSubmission = () => {
    cy.intercept('GET', '/api/session', {
      fixture: 'sessionsCreatedOne.json',
    }).as('sessionsApiCallGet');

    submit();
    cy.url().should('include', sessionUrl);

    cy.contains(
      'span.mat-simple-snack-bar-content',
      'Session created !'
    ).should('exist');

    cy.wait(4000);

    cy.contains(
      'span.mat-simple-snack-bar-content',
      'Session created !'
    ).should('not.exist');

    cy.get('mat-card.item').should('have.length', 4);
  };

  const sessionCreationDataCheckOnPostSessionApiCall = () => {
    createSessionPageView();
    validTextCreateSessionForm();
    validDropdownSelectionCreateSessionForm();
    correctFormSubmission();

    cy.wait('@sessionApiCallPost').then((interception) => {
      expect(interception.request.body).to.have.property('date', '2024-01-01');
      expect(interception.request.body).to.have.property(
        'description',
        'description'
      );
      expect(interception.request.body).to.have.property('name', 'name');
      expect(interception.request.body).to.have.property('teacher_id', 10);
    });
  };

  it('create session page view', createSessionPageView);
  it(
    'Create Session page should have form init correctly',
    shouldHaveFormInitCorrect
  );
  it('should have incorrect text form fields', invalidTextCreateSessionForm);
  it('should have correct text form fields', validTextCreateSessionForm);
  it(
    'should have correct number and content of mat-option in dropdown teacher selection menu',
    validDropdownSelectionCreateSessionForm
  );
  it('should submit and create session', correctFormSubmission);
  it(
    'should have correct data in session creation POST API request',
    sessionCreationDataCheckOnPostSessionApiCall
  );
});

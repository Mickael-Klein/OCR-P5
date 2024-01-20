describe('Register spec', () => {
  const registerUrl = '/register';

  const clickAndSubmit = () => {
    cy.get('input[formControlName=firstName]').click();
    cy.get('input[formControlName=lastName]').click();
    cy.get('input[formControlName=email]').click();
    cy.get('input[formControlName=password]').click().type(`{enter}{enter}`);
  };

  const typeAndSubmit = (
    firstName: string,
    lastName: string,
    email: string,
    password: string
  ) => {
    cy.get('input[formControlName=firstName]').type(firstName);
    cy.get('input[formControlName=lastName]').type(lastName);
    cy.get('input[formControlName=email]').type(email);
    cy.get('input[formControlName=password]').type(`${password}{enter}{enter}`);
  };

  const invalidForm = () => {
    cy.get('input[formControlName=firstName]').should(
      'have.class',
      'ng-invalid'
    );
    cy.get('input[formControlName=lastName]').should(
      'have.class',
      'ng-invalid'
    );
    cy.get('input[formControlName=email]').should('have.class', 'ng-invalid');
    cy.get('input[formControlName=password]').should(
      'have.class',
      'ng-invalid'
    );
    cy.get('button[type="submit"]').should('be.disabled');
  };

  const validForm = () => {
    cy.get('input[formControlName=firstName]').should('have.class', 'ng-valid');
    cy.get('input[formControlName=lastName]').should('have.class', 'ng-valid');
    cy.get('input[formControlName=email]').should('have.class', 'ng-valid');
    cy.get('input[formControlName=password]').should('have.class', 'ng-valid');
    cy.get('button[type="submit"]').should('not.be.disabled');
  };

  const registerSuccessTest = () => {
    cy.visit(registerUrl);

    cy.intercept('POST', '/api/auth/register', {
      body: {},
    }).as('registerApiCall');

    typeAndSubmit('firstName', 'lastName', 'yoga@studio.com', 'test!1234');

    cy.url().should('include', '/login');
  };

  const invalidRegisterFormTest = () => {
    cy.visit(registerUrl);

    cy.intercept('POST', '/api/auth/register', {
      // Server Failure - Or could be invalid email cause already used
      statusCode: 500,
      body: { error: 'Internal server error' },
    });

    clickAndSubmit();
    invalidForm();

    typeAndSubmit('f', 'l', 't', 't');
    invalidForm();

    typeAndSubmit('irst', 'ast', 'est@se', 'est123');
    validForm();

    cy.url().should('include', '/register');
  };

  const apiRegisterCallDataCheck = () => {
    registerSuccessTest();

    cy.wait('@registerApiCall').then((interception) => {
      expect(interception.request.body).to.have.property(
        'email',
        'yoga@studio.com'
      );
      expect(interception.request.body).to.have.property(
        'firstName',
        'firstName'
      );
      expect(interception.request.body).to.have.property(
        'lastName',
        'lastName'
      );
      expect(interception.request.body).to.have.property(
        'password',
        'test!1234'
      );
    });
  };

  it(
    'Should send correct datas on register API call',
    apiRegisterCallDataCheck
  );
  it('Register successful', registerSuccessTest);
  it('Invalid Register form', invalidRegisterFormTest);
});
